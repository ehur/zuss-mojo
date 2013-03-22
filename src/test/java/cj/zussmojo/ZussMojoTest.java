package cj.zussmojo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class ZussMojoTest {

    private ZussMojo mojo;
    private String testFileName = "testZussFile.zuss";
    private List<String> fileList;
    private File zussFile;

    @Before
    public void setUp() throws IOException {
        mojo = new ZussMojo();
        fileList = new ArrayList<String>();
        mojo.setInputDirectory(new File("inputDir/dir"));
        mojo.setOutputDirectory(new File("outputDir/dir"));
        zussFile = new File(mojo.getInputDirectory()+"/"+testFileName);
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(mojo.getOutputDirectory());
    }

    @Test
    public void goodFile() throws MojoExecutionException, MojoFailureException, IOException {
        FileUtils.writeStringToFile(zussFile,getTestZuss());
        fileList.add(testFileName);
        mojo.setIncludes(fileList);
        mojo.execute();
        File cssfile = new File(mojo.getOutputDirectory() + "/" + StringUtils.replace(testFileName,"zuss","css"));
        assert(cssfile.exists());
        checkFileContents(cssfile);
    }

    private void checkFileContents(File cssFile) throws IOException {
        List<String> lines = FileUtils.readLines(cssFile, "UTF-8");
        assertEquals(12,lines.size());
        assertTrue(lines.get(0).contains("#cj-status-bar{"));
    }

    @Test
    public void emptyFileList() throws MojoExecutionException, MojoFailureException {
        mojo.setIncludes(fileList);
        mojo.execute();
        File cssfile = new File(mojo.getOutputDirectory() + "/" + StringUtils.replace(testFileName,"zuss","css"));
        assertFalse(cssfile.exists());
    }

    @Test
    public void badFile() throws MojoExecutionException, MojoFailureException, IOException {
        FileUtils.writeStringToFile(zussFile,"some uncompilable stuff");
        fileList.add(testFileName);
        mojo.setIncludes(fileList);
        mojo.execute();
        File cssfile = new File(mojo.getOutputDirectory() + "/" + StringUtils.replace(testFileName,"zuss","css"));
        List<String> lines = FileUtils.readLines(cssfile, "UTF-8");
        assertEquals(0,lines.size());
    }

    private String getTestZuss(){
        String zuss = "@cj-bars(@radius: 0px) {\n" +
                "    border-top-right-radius: @radius;\n" +
                "    border-top-left-radius: @radius;\n" +
                "    background: #ffffff; /* Old browsers */\n" +
                "    width:100%;\n" +
                "}\n" +
                "\n" +
                "#cj-status-bar {\n" +
                "    @cj-bars(0px);\n" +
                "}\n " +
                "#cj-bottom-bar {\n" +
                "    @cj-bars(4px);\n" +
                "}\n ";
        return zuss;
    }
}
