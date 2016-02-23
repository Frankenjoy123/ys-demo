package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.data.object.MktDrawPrizeObject;
import com.yunsoo.common.data.object.MktDrawRecordObject;
import com.yunsoo.common.data.object.MktDrawRuleObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.MarketingEntity;
import com.yunsoo.data.service.entity.MktDrawPrizeEntity;
import com.yunsoo.data.service.entity.MktDrawRecordEntity;
import com.yunsoo.data.service.entity.MktDrawRuleEntity;
import com.yunsoo.data.service.repository.MarketingRepository;
import com.yunsoo.data.service.repository.MktDrawPrizeRepository;
import com.yunsoo.data.service.repository.MktDrawRecordRepository;
import com.yunsoo.data.service.repository.MktDrawRuleRepository;
import com.yunsoo.data.service.service.ProductService;
import com.yunsoo.data.service.service.contract.Product;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
@RestController
@RequestMapping("/marketing")
public class MarketingController {

    @Autowired
    private MarketingRepository marketingRepository;

    @Autowired
    private MktDrawRuleRepository mktDrawRuleRepository;

    @Autowired
    private MktDrawPrizeRepository mktDrawPrizeRepository;

    @Autowired
    private MktDrawRecordRepository mktDrawRecordRepository;

    @Autowired
    private ProductService productService;

    //get marketing plan by id, provide API
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public MarketingObject getMarketingById(@PathVariable String id) {
        MarketingEntity entity = findMarketingById(id);
        return toMarketingObject(entity);
    }

    //get mktDrawPrize by product key, provide API-Rabbit
    @RequestMapping(value = "/drawprize/{key}", method = RequestMethod.GET)
    public MktDrawPrizeObject getMktDrawPrizeByProductKey(@PathVariable String key) {
        List<MktDrawPrizeEntity> entities = mktDrawPrizeRepository.findByProductKey(key);
        if (entities.size() > 0) {
            MktDrawPrizeEntity entity = entities.get(0);
            return toMktDrawPrizeObject(entity);
        } else {
            return null;
        }
    }

    //get mktDrawRecord by product key, provide API-Rabbit
    @RequestMapping(value = "/draw/{key}", method = RequestMethod.GET)
    public MktDrawRecordObject getMktDrawRecordByProductKey(@PathVariable String key) {
        List<MktDrawRecordEntity> entities = mktDrawRecordRepository.findByProductKey(key);
        if (entities.size() > 0) {
            MktDrawRecordEntity entity = entities.get(0);
            return toMktDrawRecordObject(entity);
        } else {
            return null;
        }
    }

    //query marketing plan, provide API
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<MarketingObject> getByFilter(@RequestParam(value = "org_id") String orgId,
                                             Pageable pageable,
                                             HttpServletResponse response) {
        Page<MarketingEntity> entityPage = marketingRepository.findByOrgId(orgId, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }

        return entityPage.getContent().stream()
                .map(this::toMarketingObject)
                .collect(Collectors.toList());
    }

    //create marketing plan, provide API
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MarketingObject createMarketing(@RequestBody MarketingObject marketingObject) {
        MarketingEntity entity = toMarketingEntity(marketingObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);
        MarketingEntity newEntity = marketingRepository.save(entity);
        return toMarketingObject(newEntity);
    }

    //create marketing draw record by key, provide: API-Rabbit
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRecordObject createDrawRecord(@RequestBody MktDrawRecordObject mktDrawRecordObject) {
        String productKey = mktDrawRecordObject.getProductKey();
        Product product = productService.getByKey(productKey);
        if (product == null) {
            throw new NotFoundException("Product");
        }
        List<MktDrawRecordEntity> mktDrawRecordEntityList = mktDrawRecordRepository.findByProductKey(productKey);
        if (mktDrawRecordEntityList.size() > 0) {
            throw new BadRequestException("This product has been already drawed.");
        }

        MktDrawRecordEntity entity = toMktDrawRecordEntity(mktDrawRecordObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        MktDrawRecordEntity newEntity = mktDrawRecordRepository.save(entity);

        return toMktDrawRecordObject(newEntity);
    }

    //create marketing draw prize by draw record id, provide: API-Rabbit
    @RequestMapping(value = "/drawPrize", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawPrizeObject createDrawPrize(@RequestBody MktDrawPrizeObject mktDrawPrizeObject) {
        String drawRecordId = mktDrawPrizeObject.getDrawRecordId();

        MktDrawRecordEntity mktDrawRecordEntity = mktDrawRecordRepository.findOne(drawRecordId);
        if (mktDrawRecordEntity == null) {
            throw new NotFoundException("This draw record has not been found.");
        }

        MktDrawPrizeEntity entity = toMktDrawPrizeEntity(mktDrawPrizeObject);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setStatusCode(LookupCodes.MktDrawPrizeStatus.CREATED);
        entity.setScanRecordId(mktDrawRecordEntity.getScanRecordId());
        entity.setProductKey(mktDrawRecordEntity.getProductKey());
        MktDrawPrizeEntity newEntity = mktDrawPrizeRepository.save(entity);

        MarketingEntity marketing = marketingRepository.findOne(mktDrawPrizeObject.getMarketingId());
        marketing.setBalance(marketing.getBalance() - mktDrawPrizeObject.getAmount());
        marketingRepository.save(marketing);


        return toMktDrawPrizeObject(newEntity);
    }

    //update marketing draw prize by product key, provide: API-Rabbit
    @RequestMapping(value = "/drawPrize", method = RequestMethod.PUT)
    public MktDrawPrizeObject updateDrawPrize(@RequestBody MktDrawPrizeObject mktDrawPrizeObject) {

        String productKey = mktDrawPrizeObject.getProductKey();
        List<MktDrawPrizeEntity> entities = mktDrawPrizeRepository.findByProductKey(productKey);
        if (entities.size() == 0) {
            throw new NotFoundException("This draw prize has not been found");
        }

        MktDrawPrizeEntity entity = entities.get(0);
        if(!entity.getDrawRecordId().equals(mktDrawPrizeObject.getDrawRecordId()))
            throw new NotFoundException("This draw prize has not been found");

        entity.setAccountType(mktDrawPrizeObject.getAccountType());
        entity.setPrizeAccount(mktDrawPrizeObject.getPrizeAccount());
        entity.setPrizeAccountName(mktDrawPrizeObject.getPrizeAccountName());
        entity.setStatusCode(LookupCodes.MktDrawPrizeStatus.SUBMIT);
        MktDrawPrizeEntity newEntity = mktDrawPrizeRepository.save(entity);

        return toMktDrawPrizeObject(newEntity);
    }

    //create marketing draw rule provide API
    @RequestMapping(value = "/drawRule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRuleObject createDrawRule(@RequestBody MktDrawRuleObject mktDrawRuleObject) {
        MktDrawRuleEntity entity = toMktDrawRuleEntity(mktDrawRuleObject);
        MktDrawRuleEntity newEntity = mktDrawRuleRepository.save(entity);
        return toMktDrawRuleObject(newEntity);
    }

    //delete marketing plan
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarketing(@PathVariable(value = "id") String id) {
        MarketingEntity entity = marketingRepository.findOne(id);
        if (entity != null) {
            marketingRepository.delete(id);
        }
    }

    //delete marketing rule by marketing Id
    @RequestMapping(value = "/drawRule/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarketingRuleByMarketingId(@PathVariable(value = "id") String id) {
        if (id != null) {
            List<MktDrawRuleEntity> mktDrawRuleEntities = mktDrawRuleRepository.findByMarketingId(id);
            if (mktDrawRuleEntities.size() > 0) {
                for (MktDrawRuleEntity entity : mktDrawRuleEntities) {
                    String mktDrawRuleId = entity.getId();
                    mktDrawRuleRepository.delete(mktDrawRuleId);
                }
            }
        }
    }

    @RequestMapping(value = "/drawRule/{id}", method = RequestMethod.GET)
    public List<MktDrawRuleObject> findMarketingRulesById(@PathVariable(value = "id")String marketingId){
        List<MktDrawRuleEntity> mktDrawRuleEntities = mktDrawRuleRepository.findByMarketingId(marketingId);
        return mktDrawRuleEntities.stream().map(this::toMktDrawRuleObject).collect(Collectors.toList());
    }

    private MarketingEntity findMarketingById(String id) {
        MarketingEntity entity = marketingRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("marketing not found by [id: " + id + ']');
        }
        return entity;
    }

    private MarketingObject toMarketingObject(MarketingEntity entity) {
        if (entity == null) {
            return null;
        }
        MarketingObject object = new MarketingObject();
        object.setId(entity.getId());
        object.setName(entity.getName());
        object.setOrgId(entity.getOrgId());
        object.setProductBaseId(entity.getProductBaseId());
        object.setTypeCode(entity.getTypeCode());
        object.setBudget(entity.getBudget());
        object.setBalance(entity.getBalance());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }

    private MktDrawRuleObject toMktDrawRuleObject(MktDrawRuleEntity entity) {
        if (entity == null) {
            return null;
        }
        MktDrawRuleObject object = new MktDrawRuleObject();
        object.setId(entity.getId());
        object.setMarketingId(entity.getMarketingId());
        object.setPrizeTypeCode(entity.getPrizeTypeCode());
        object.setAmount(entity.getAmount());
        object.setProbability(entity.getProbability());
        object.setComments(entity.getComments());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }

    private MktDrawRecordObject toMktDrawRecordObject(MktDrawRecordEntity entity) {
        if (entity == null) {
            return null;
        }
        MktDrawRecordObject object = new MktDrawRecordObject();
        object.setId(entity.getId());
        object.setScanRecordId(entity.getScanRecordId());
        object.setMarketingId(entity.getMarketingId());
        object.setProductBaseId(entity.getProductBaseId());
        object.setProductKey(entity.getProductKey());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setUserId(entity.getUserId());
        object.setIsPrized(entity.getIsPrized());
        return object;
    }

    private MktDrawPrizeObject toMktDrawPrizeObject(MktDrawPrizeEntity entity) {
        if (entity == null) {
            return null;
        }
        MktDrawPrizeObject object = new MktDrawPrizeObject();
        object.setDrawRecordId(entity.getDrawRecordId());
        object.setProductKey(entity.getProductKey());
        object.setScanRecordId(entity.getScanRecordId());
        object.setMarketingId(entity.getMarketingId());
        object.setDrawRuleId(entity.getDrawRuleId());
        object.setAmount(entity.getAmount());
        object.setMobile(entity.getMobile());
        object.setPrizeTypeCode(entity.getPrizeTypeCode());
        object.setStatusCode(entity.getStatusCode());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setPaidDateTime(entity.getPaidDateTime());
        object.setAccountType(entity.getAccountType());
        object.setPrizeAccount(entity.getPrizeAccount());
        object.setPrizeAccountName(entity.getPrizeAccountName());
        object.setComments(entity.getComments());
        return object;
    }


    private MarketingEntity toMarketingEntity(MarketingObject object) {
        if (object == null) {
            return null;
        }
        MarketingEntity entity = new MarketingEntity();
        entity.setId(object.getId());
        entity.setName(object.getName());
        entity.setOrgId(object.getOrgId());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setTypeCode(object.getTypeCode());
        entity.setBudget(object.getBudget());
        entity.setBalance(object.getBalance());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }

    private MktDrawRuleEntity toMktDrawRuleEntity(MktDrawRuleObject object) {
        if (object == null) {
            return null;
        }
        MktDrawRuleEntity entity = new MktDrawRuleEntity();
        entity.setId(object.getId());
        entity.setMarketingId(object.getMarketingId());
        entity.setPrizeTypeCode(object.getPrizeTypeCode());
        entity.setAmount(object.getAmount());
        entity.setProbability(object.getProbability());
        entity.setComments(object.getComments());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }


    private MktDrawRecordEntity toMktDrawRecordEntity(MktDrawRecordObject object) {
        if (object == null) {
            return null;
        }
        MktDrawRecordEntity entity = new MktDrawRecordEntity();
        entity.setId(object.getId());
        entity.setScanRecordId(object.getScanRecordId());
        entity.setMarketingId(object.getMarketingId());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setProductKey(object.getProductKey());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setUserId(object.getUserId());
        entity.setIsPrized(object.getIsPrized());
        return entity;
    }

    private MktDrawPrizeEntity toMktDrawPrizeEntity(MktDrawPrizeObject object) {
        if (object == null) {
            return null;
        }
        MktDrawPrizeEntity entity = new MktDrawPrizeEntity();
        entity.setDrawRecordId(object.getDrawRecordId());
        entity.setProductKey(object.getProductKey());
        entity.setScanRecordId(object.getScanRecordId());
        entity.setMarketingId(object.getMarketingId());
        entity.setDrawRuleId(object.getDrawRuleId());
        entity.setAmount(object.getAmount());
        entity.setMobile(object.getMobile());
        entity.setPrizeTypeCode(object.getPrizeTypeCode());
        entity.setStatusCode(object.getStatusCode());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setPaidDateTime(object.getPaidDateTime());
        entity.setAccountType(object.getAccountType());
        entity.setPrizeAccount(object.getPrizeAccount());
        entity.setPrizeAccountName(object.getPrizeAccountName());
        entity.setComments(object.getComments());
        return entity;
    }


}
