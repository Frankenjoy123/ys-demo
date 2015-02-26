///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.com.yunsoo.api.dao;
//
//import com.com.yunsoo.api.dbmodel.LogisticsCheckPathModel;
//import com.com.yunsoo.api.dbmodel.LogisticsCheckPointModel;
//import com.com.yunsoo.api.dbmodel.LogisticsKeyTrackingModel;
//import com.com.yunsoo.api.dbmodel.LogisticsTrackingModel;
//import java.util.List;
//
///**
// *
// * @author qyu
// */
//public interface LogisticsDao {
//
//    public LogisticsTrackingModel getTrackingInfo(String key);
//
//    public long createTracking(LogisticsTrackingModel model);
//
//    public DaoStatus updateTracking(LogisticsTrackingModel model);
//
//    public DaoStatus deleteTracking(LogisticsTrackingModel model);
//
//    public LogisticsCheckPointModel getPoint(int id);
//
//    public DaoStatus createPoint(LogisticsCheckPointModel model);
//
//    public DaoStatus updatePoint(LogisticsCheckPointModel model);
//
//    public DaoStatus deletePoint(LogisticsCheckPointModel model);
//
//    public List<LogisticsCheckPointModel> getPointsByOrgId(int org_id);
//
//    public LogisticsCheckPathModel getPath(long id);
//
//    public DaoStatus createPath(LogisticsCheckPathModel model);
//
//    public DaoStatus updatePath(LogisticsCheckPathModel model);
//
//    public DaoStatus deletePath(LogisticsCheckPathModel model);
//
//    public List<LogisticsCheckPathModel> getPathsByTrackingId(long trackingId);
//}
