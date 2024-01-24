package com.aemvertis.core.testcontext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.testing.mock.sling.ResourceResolverType;

public final class VertisAemContext {
    private VertisAemContext() {
    }

    public static final String VERTIS_SLING_MODELS = "com.geeks.aem.tutorials.core.models";
    public static final String SLASH = "/";
    public static final String PERIOD = ".";
    public static final String JSON_FILE = "json";

    public static AemContext newAemContext() {
        return newAemContext(null);
    }

    public static AemContext newAemContext(final String currentResource, final String... paths) {
        return new AemContextBuilder().resourceResolverType(
                ResourceResolverType.RESOURCERESOLVER_MOCK).<AemContext>afterSetUp(context -> {
            context.addModelsForPackage(VERTIS_SLING_MODELS);

            if (paths != null) {
                int index = 0;
                for (String path : paths) {
                    String fileName = path + "/"
                            + path.split(SLASH)[path.split(SLASH).length - 1]
                            + PERIOD + JSON_FILE;
                    context.load().json(fileName, path);

                    if (index == 0 && StringUtils.isNotBlank(currentResource)) {
                        context.currentResource(path + currentResource);
                    }
                    index++;
                }
            }
        }).build();
    }
}
