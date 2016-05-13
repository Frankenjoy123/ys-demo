package com.yunsoo.data.service.entity;

import com.yunsoo.common.data.object.MarketUserLocationAnalysisObject;
import com.yunsoo.common.data.object.MarketUserUsageAnalysisObject;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Dake Wang on 2016/5/12.
 */
@Entity
@Table(name = "emr_market_user_location_analysis")
public class MarketUserLocationAnalysisEntity {

    @Column(name = "id")
    @Id
    private int id;

    @Column(name = "draw_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime drawDate;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "count")
    private int count;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "marketing_id")
    private String marketingId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DateTime getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(DateTime drawDate) {
        this.drawDate = drawDate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public static MarketUserLocationAnalysisObject toDataObject(MarketUserLocationAnalysisEntity entity) {
        MarketUserLocationAnalysisObject object = new MarketUserLocationAnalysisObject();
        object.setDrawDate(entity.getDrawDate());
        object.setOrgId(entity.getOrgId());
        object.setMarketingId(entity.getMarketingId());
        object.setCount(entity.getCount());
        object.setProvince(entity.getProvince());
        object.setCity(entity.getCity());
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketUserLocationAnalysisEntity)) return false;

        MarketUserLocationAnalysisEntity entity = (MarketUserLocationAnalysisEntity) o;

        String key = getUniqueKey();
        return key.equals(entity.getUniqueKey());
    }

    @Override
    public int hashCode() {
        return getUniqueKey().hashCode();
    }

    //TODO 解决为啥要放在这里。
    public String getUniqueKey() {
        return drawDate.toString("yyyy-MM-dd") + orgId + marketingId + province + city;
    }
}
