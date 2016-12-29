package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by yan on 11/22/2016.
 */
public class WeChatRedPackRequest {

    @JsonProperty("order_id")
    @NotEmpty
    private String id;

    @JsonProperty("mch_name")
    @NotEmpty
    private String mchName;  //商家名字

    @JsonProperty("price")
    @NotNull
    @Min(value = 1)
    private BigDecimal price;

    @JsonProperty("openid")
    @NotEmpty
    private String openId;

    @JsonProperty("wishing")
    @NotEmpty
    private String wishing;  //祝福语

    @JsonProperty("remark")
    @NotEmpty
    private String remark;   //备注

    @JsonProperty("action_name")
    @NotEmpty
    private String actionName;   //活动名字

    @JsonProperty("org_id")
    private String orgId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWishing() {
        return wishing;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }
}
