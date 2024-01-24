package com.aemvertis.core.models;

import com.aemvertis.core.pojo.MegaMenuPojo;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class MegaMenuModel {

    @ScriptVariable
    private Page currentPage;

    @SlingObject
    private ResourceResolver resolver;

    private List<MegaMenuPojo> menuList;

    private static final int MAX_DEPTH = 3;

    private static final Logger LOG = LoggerFactory.getLogger(MegaMenuModel.class);

    @PostConstruct
    protected void init() {
        menuList = new ArrayList<>();
        InheritanceValueMap ivm = new HierarchyNodeInheritanceValueMap(currentPage.getContentResource());
        String inheritedValueCustomProperty = ivm.getInherited("rootPagePath", String.class);
        if (StringUtils.isNotEmpty(inheritedValueCustomProperty)) {
            Resource parentPageResource = resolver.getResource(inheritedValueCustomProperty);
            Page parentPage = parentPageResource.adaptTo(Page.class);
            menuList = getMegaMenuPageList(parentPage, 1); // Start with depth 1
        }
    }

    List<MegaMenuPojo> getMegaMenuPageList(final Page page, int depth) {
        List<MegaMenuPojo> childMegaMenuList = new ArrayList<>();
        if (depth > MAX_DEPTH) {
            return childMegaMenuList; // Stop recursion if depth exceeds the maximum
        }

        Iterator<Page> childPageList = page.listChildren();
        while (childPageList.hasNext()) {
            MegaMenuPojo megaMenuPojo = new MegaMenuPojo();
            Page childPage = childPageList.next();
            ValueMap valueMap = childPage.getProperties();
            if (valueMap.containsKey("hideInNav")
                    && valueMap.get("hideInNav", Boolean.class)) {
                LOG.info("Page is hidden in Navigation");
            } else {
                megaMenuPojo.setPageTitle(childPage.getTitle());
                megaMenuPojo.setPageLink(childPage.getPath());
                if (valueMap.containsKey("hideAllSubPagesInNav")
                        && valueMap.get("hideAllSubPagesInNav", Boolean.class)) {
                    LOG.info("Child Pages are hidden in Navigation");
                } else {
                    List<MegaMenuPojo> megaMenuList = getMegaMenuPageList(childPage, depth + 1);
                    megaMenuPojo.setChildPageList(megaMenuList);
                }
                childMegaMenuList.add(megaMenuPojo);
            }
        }
        return childMegaMenuList;
    }

    public List<MegaMenuPojo> getMenuList() {
        return menuList;
    }
}
