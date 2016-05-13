package com.yunsoo.data.service.entity;

import com.yunsoo.common.data.object.MarketUserAreaAnalysisObject;
import com.yunsoo.common.data.object.ScanRecordAnalysisObject;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Dake Wang on 2016/5/11.
 */
@Entity
@Table(name = "emr_market_user_area_analysis")
public class MarketUserAreaAnalysisEntity {

    @Column(name = "id")
    @Id
    private int id;

    @Column(name = "draw_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime drawDate;

    @Column(name = "tag_id")
    private int tagId;

    @Column(name = "count")
    private int count;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "marketing_id")
    private String marketingId;

    @Column(name = "tag_name")
    private String tagName;


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

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public static MarketUserAreaAnalysisObject toDataObject(MarketUserAreaAnalysisEntity entity) {
        MarketUserAreaAnalysisObject object = new MarketUserAreaAnalysisObject();
        object.setDrawDate(entity.getDrawDate());
        object.setOrgId(entity.getOrgId());
        object.setMarketingId(entity.getMarketingId());
        object.setCount(entity.getCount());
        object.setTagId(entity.getTagId());
        object.setTagName(entity.getTagName());
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketUserAreaAnalysisEntity)) return false;

        MarketUserAreaAnalysisEntity entity = (MarketUserAreaAnalysisEntity) o;

        String key = getUniqueKey();
        return key.equals(entity.getUniqueKey());
    }

    @Override
    public int hashCode() {
        return getUniqueKey().hashCode();
    }

    public String getUniqueKey() {
        return drawDate.toString("yyyy-MM-dd") + orgId + marketingId + tagId;
    }
}
