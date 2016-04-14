package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-04-13
 * Descriptions:
 */
public class OrganizationConfigObject implements Serializable {

    public static final String VERSION = "1.0";

    @JsonProperty("version")
    private String version;

    @JsonProperty("enterprise")
    private Enterprise enterprise;

    @JsonProperty("modified_account_id")
    private String modifiedAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
    private DateTime modifiedDateTime;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public String getModifiedAccountId() {
        return modifiedAccountId;
    }

    public void setModifiedAccountId(String modifiedAccountId) {
        this.modifiedAccountId = modifiedAccountId;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    @com.fasterxml.jackson.annotation.JsonIgnoreType
    public static class Version {

        @JsonProperty("version")
        private String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
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
