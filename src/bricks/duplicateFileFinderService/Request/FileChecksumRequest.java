/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bricks.duplicateFileFinderService.Request;

import java.util.ArrayList;
import java.util.Map;

public interface FileChecksumRequest {
    public ArrayList<String> getFileList();
    public void addChecksumResult(String file, String hash);
}
