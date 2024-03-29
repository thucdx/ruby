/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.intent.detection;

import fpt.qa.intent.detection.qc.VnIntentDetection;

/**
 *
 * @author ngan
 */
public class TVIntentDetection {

    static VnIntentDetection classifier;

    public static void init(String qcDir, String dictDir) {
        FreqConjDict.loadConjList(dictDir + "/tv_conjunctions.txt");
        classifier = new VnIntentDetection(qcDir);
        classifier.init();
    }

    public static String getIntent(String sent) {
    	return classifier.classify(sent);
    }
}
