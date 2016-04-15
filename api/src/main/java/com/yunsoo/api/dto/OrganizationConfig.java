package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.OrganizationConfigObject;

/**
 * Created by:   Lijian
 * Created on:   2016-04-13
 * Descriptions:
 */
public class OrganizationConfig {

    @JsonProperty("organization")
    private Organization organization;

    @JsonProperty("enterprise")
    private Enterprise enterprise;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }


    public OrganizationConfig() {
    }

    public OrganizationConfig(OrganizationConfigObject configObject) {
        if (configObject.getEnterprise() != null) {
            OrganizationConfig.Enterprise enterprise = new OrganizationConfig.Enterprise();
            enterprise.setName(configObject.getEnterprise().getName());
            if (configObject.getEnterprise().getLogin() != null) {
                OrganizationConfig.Enterprise.Login login = new OrganizationConfig.Enterprise.Login();
                login.setBackgroundImage(configObject.getEnterprise().getLogin().getBackgroundImage());
                if (configObject.getEnterprise().getLogin().getBandApplicationLink() != null) {
                    OrganizationConfig.Enterprise.Login.BandApplicationLink bandApplicationLink = new OrganizationConfig.Enterprise.Login.BandApplicationLink();
                    bandApplicationLink.setName(configObject.getEnterprise().getLogin().getBandApplicationLink().getName());
                    bandApplicationLink.setUrl(configObject.getEnterprise().getLogin().getBandApplicationLink().getUrl());
                    login.setBandApplicationLink(bandApplicationLink);
                }
                enterprise.setLogin(login);
            }
            this.setEnterprise(enterprise);
        }
    }

    public OrganizationConfigObject toOrganizationConfigObject() {
        OrganizationConfigObject config = new OrganizationConfigObject();
        if (this.getEnterprise() != null) {
            OrganizationConfigObject.Enterprise enterprise = new OrganizationConfigObject.Enterprise();
            enterprise.setName(this.getEnterprise().getName());
            if (this.getEnterprise().getLogin() != null) {
                OrganizationConfigObject.Enterprise.Login login = new OrganizationConfigObject.Enterprise.Login();
                login.setBackgroundImage(this.getEnterprise().getLogin().getBackgroundImage());
                if (this.getEnterprise().getLogin().getBandApplicationLink() != null) {
                    OrganizationConfigObject.Enterprise.Login.BandApplicationLink bandApplicationLink = new OrganizationConfigObject.Enterprise.Login.BandApplicationLink();
                    bandApplicationLink.setName(this.getEnterprise().getLogin().getBandApplicationLink().getName());
                    bandApplicationLink.setUrl(this.getEnterprise().getLogin().getBandApplicationLink().getUrl());
                    login.setBandApplicationLink(bandApplicationLink);
                }
                enterprise.setLogin(login);
            }
            config.setEnterprise(enterprise);
        }
        return config;
    }

    public static class Enterprise {

        @JsonProperty("name")
        private String name;

        @JsonProperty("login")
        private Login login;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Login getLogin() {
            return login;
        }

        public void setLogin(Login login) {
            this.login = login;
        }


        public static class Login {

            @JsonProperty("background_image")
            private String backgroundImage;

            @JsonProperty("band_application_link")
            private BandApplicationLink bandApplicationLink;

            public String getBackgroundImage() {
                return backgroundImage;
            }

            public void setBackgroundImage(String backgroundImage) {
                this.backgroundImage = backgroundImage;
            }


            public BandApplicationLink getBandApplicationLink() {
                return bandApplicationLink;
            }

            public void setBandApplicationLink(BandApplicationLink bandApplicationLink) {
                this.bandApplicationLink = bandApplicationLink;
            }

            public static class BandApplicationLink {

                @JsonProperty("name")
                private String name;

                @JsonProperty("url")
                private String url;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

        }
    }

}
