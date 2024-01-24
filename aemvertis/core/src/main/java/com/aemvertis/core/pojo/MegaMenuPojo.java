package com.aemvertis.core.pojo;

import java.util.List;

public class MegaMenuPojo {

    private String pageTitle;

    private String pageLink;

    private List<MegaMenuPojo> childPageList;

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    public List<MegaMenuPojo> getChildPageList() {
        return childPageList;
    }

    public void setChildPageList(List<MegaMenuPojo> childPageList) {
        this.childPageList = childPageList;
    }
}
