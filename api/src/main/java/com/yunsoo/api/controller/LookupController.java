package com.yunsoo.api.controller;

import com.yunsoo.api.domain.LookupDomain;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.web.util.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 9/9/2015.
 */
@RestController
@RequestMapping("/lookup")
public class LookupController {

    @Autowired
    private LookupDomain domain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Lookup> getByFilter(@RequestParam(value = "type_code", required = false) String typeCode,
                                                  @RequestParam(value = "active", required = false) Boolean active,
                                                  Pageable pageable,
                                                  HttpServletResponse response){
        LookupCodes.LookupType type = LookupCodes.LookupType.toLookupType(typeCode);
        if(type!=null)
            return domain.getLookupListByType(type, active);
        else {
            List<Lookup> lookupList = domain.getAllLookup().stream().map(Lookup::new).collect(Collectors.toList());
            if (active != null)
                lookupList = lookupList.stream().filter(item -> active.equals(item.getActive())).collect(Collectors.toList());

            if(pageable != null )
            {
                int totalSize = lookupList.size();
                response.setHeader("Content-Range", PageableUtils.formatPages(pageable.getPageNumber(), totalSize/pageable.getPageSize() + ((totalSize%pageable.getPageSize()) > 0?1:0)));
                if(totalSize > (pageable.getPageNumber()+1) * pageable.getPageSize())
                    return lookupList.subList(pageable.getPageNumber() * pageable.getPageSize(), (pageable.getPageNumber()+1) * pageable.getPageSize());
                else
                    return lookupList.subList(pageable.getPageNumber() * pageable.getPageSize(), totalSize);
            }
            else
                return lookupList;

        }
    }

    @RequestMapping(value = "/typeCodes", method = RequestMethod.GET)
    public List<String> getTypeCodes(){
        return domain.getAllLookup().stream().map(LookupObject::getTypeCode).distinct().collect(Collectors.toList());

    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void save(@RequestBody Lookup lookup){
       domain.save(lookup);
    }

}
