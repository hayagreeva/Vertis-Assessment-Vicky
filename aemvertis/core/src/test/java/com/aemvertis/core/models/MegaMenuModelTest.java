package com.aemvertis.core.models;

import com.aemvertis.core.pojo.MegaMenuPojo;
import com.aemvertis.core.testcontext.VertisAemContext;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class MegaMenuModelTest {

    private final AemContext aemContext = VertisAemContext.newAemContext();

    private MegaMenuModel megaMenuModel;

    private Page currentPage;

    private Resource resource;

    public static final String HOME_PAGE_PATH = "/content/aemvertis/us/en/home";
    public static final String HOME_PAGE_JSON = "/content/aemvertis/home/home.json";
    public static final String SLASH = "/";
    public static final String JCR_CONTENT = "jcr:content";
    public static final String MEGAMENU_COMP = "/root/container/megamenu";
    public static final int EIGHT = 8;

    @BeforeEach
    public void setup() {
        aemContext.addModelsForClasses(MegaMenuModel.class);
        aemContext.load().json(HOME_PAGE_JSON, HOME_PAGE_PATH);
        aemContext.load().json(HOME_PAGE_JSON, HOME_PAGE_PATH + SLASH + JCR_CONTENT + MEGAMENU_COMP);
        currentPage = aemContext.currentPage(HOME_PAGE_PATH);
        aemContext.currentResource(HOME_PAGE_PATH + SLASH + JCR_CONTENT + MEGAMENU_COMP);
        resource = aemContext.request().getResource();
    }

    @Test
    void testInit() {
        megaMenuModel = aemContext.request().adaptTo(MegaMenuModel.class);
        List<MegaMenuPojo> megaMenuPojoList = megaMenuModel.getMenuList();
        assertEquals(megaMenuPojoList.size(), EIGHT);
    }
}
