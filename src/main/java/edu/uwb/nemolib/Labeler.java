package edu.uwb.nemolib;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * The Labeler class combines g6 subgraph labels into canonical subgraph labels
 * using Brandon McKay's Nauty algorithm and the Nauty Traces labelg
 * implementation of that algorithm.
 *
 * Presently, nemolib requires client programs to install labelg in the
 * directory of execution for their programs. We hope to create a Java
 * implementation of the Nauty algorithm in a future release in order to
 * remove this requirement.
 */
public final class Labeler {

	// class variable
	static int instanceCounter = 0;
	
    // labelg program options
    private static final String labelGPath = "src/main/resources/labelg";
    private static final int invariant = 3;
    private static final int mininvarlevel = 1;
    private static final int maxinvarlevel = 100;

    // file options
    private static final String filePrefix  = ".";
    private static final String filePostfix = ".g6";

    // data members
    private final String inputFilename;
    private final String outputFilename;
    private final String[] args;

    /**
     * Construct a labeler object. By default, will search for the labelg
     * binary in the directory of execution.
     */
    public Labeler() {
	    instanceCounter++;
	    long currentTime = System.currentTimeMillis();
	    this.inputFilename = filePrefix + "rawgraph6_" +
			    currentTime + filePostfix + instanceCounter;
	    this.outputFilename = filePrefix + "canonical_" +
			    currentTime + filePostfix + instanceCounter;
	    this.args = getArgs();
    }

    private String[] getArgs() {
        String[] args = {
            labelGPath,
            "-i" + invariant,
            "-I" + mininvarlevel + ":" + maxinvarlevel,
            inputFilename,
            outputFilename};
        return args;
    }

    // TODO possibly remove this method; does not appear to be used
    private Map<String, Set<Double>> g6toCanonical(Map<String,
            Set<Double>> labelRelFreqsMap) {
        Map<String, String> g6CanLabelMap =
                getCanonicalLabels(labelRelFreqsMap.keySet());
        Map<String, Set<Double>> result = new HashMap<>();
        for (Map.Entry<String, Set<Double>> labelRelFreqs:
                labelRelFreqsMap.entrySet()) {
            String canLabel = g6CanLabelMap.get(labelRelFreqs.getKey());
            Set<Double> currentRelFreqs = labelRelFreqs.getValue();
            if (result.containsKey(canLabel)) {
                currentRelFreqs.addAll(result.get(canLabel));
            }
            result.put(canLabel, currentRelFreqs);
        }
        return result;
    }

	// Get canonical labels using the labelg program.
	// This function communicates with labelg using input and output files.
	//
	// code adapted from Vartika Verma's Nemo Finder project (UWB 2014)
    public Map<String, String> getCanonicalLabels( Set<String> labels ) {

		// must use LinkedHashMap to preserve ordering
		Map<String, String> results = new LinkedHashMap<>();
		
        BufferedWriter writer = null;
        BufferedReader inputReader = null;
        BufferedReader outputReader = null;
        int returnCode = 0;
        try {
            writer = new BufferedWriter(new FileWriter(inputFilename));
            for (String label : labels ) {
                writer.write(label);
                writer.write('\n');
				results.put(label, "");
            }
            writer.close();

            Process labelg = Runtime.getRuntime().exec(args);

            // close output stream, input stream & error stream
            labelg.getOutputStream().close();
            closeInputStream(labelg.getInputStream());
            closeInputStream(labelg.getErrorStream());

            // wait for labelg to complete execution
            returnCode = labelg.waitFor();

            // read back in the input and output file
            inputReader = new BufferedReader(new FileReader(inputFilename));
            outputReader = new BufferedReader(new FileReader(outputFilename));
            String inputLine = inputReader.readLine();
            String outputLine = outputReader.readLine();

            // combine the input and output, assuming labelg writes output
            // in the same order as the input is provided
            while (outputLine != null) {
                results.put(inputLine, outputLine);
                inputLine = inputReader.readLine();
                outputLine = outputReader.readLine();
            }
            inputReader.close();
            outputReader.close();
        } catch (Exception e) {
            System.err.println("Exception " + e + " raised");
            e.printStackTrace();
            results = null;
        } finally {
            try { writer.close(); } catch (Exception e) {}
            try { inputReader.close(); } catch (Exception e) {}
            try { outputReader.close(); } catch (Exception e) {}
            deleteFile(inputFilename);
            deleteFile(outputFilename);
        }

        if (results == null) {
            System.exit(-1);
        }

        if (returnCode != 0) {
            System.err.println("`labelg` exited with a return code of: " +
                returnCode);
            System.err.print("`labelg` executed with: ");
            for (String line:args) {
                System.err.print(line + " ");
            }
            System.err.println();
            System.exit(-1);
        }

        return results;
    }

    private void closeInputStream(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        String l = r.readLine();
        while (l != null) {
            l = r.readLine();
        }
        r.close();
    }

    private void deleteFile(String filename) {
        try {
            File f = new File(filename);
            f.delete();
        } catch (Exception e) {
            System.err.println("Exception " + e +
                " raised when attempting to delete " + filename);
        }
    }
}
