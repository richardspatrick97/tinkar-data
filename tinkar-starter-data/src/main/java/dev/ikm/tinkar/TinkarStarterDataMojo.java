package dev.ikm.tinkar;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "load-data", requiresDependencyResolution = ResolutionScope.RUNTIME_PLUS_SYSTEM, defaultPhase = LifecyclePhase.COMPILE)
public class TinkarStarterDataMojo extends AbstractMojo {
    @Parameter(name = "dataStore", required = true)
    private String dataStore;

    @Parameter(name = "exportFile", required = true)
    private String exportFile;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            String[] args = new String[2];
            args[0] = dataStore;
            args[1] = exportFile;
            TinkarStarterData starterData = new TinkarStarterData(args);
            starterData.execute();
        } catch (Exception e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
