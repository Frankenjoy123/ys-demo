package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 12/21/2016.
 */
public class WeChatQRCodeRequest {

    @JsonProperty("expire_seconds")
    private int expiresSeconds;

    @JsonProperty("action_name")
    private String actionName;

    @JsonProperty("action_info")
    private WeChatAction actionInfo;

    public int getExpiresSeconds() {
        return expiresSeconds;
    }

    public void setExpiresSeconds(int expiresSeconds) {
        this.expiresSeconds = expiresSeconds;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public WeChatAction getActionInfo() {
        return actionInfo;
    }

    public void setActionInfo(String scenerioId, String scenerioStr) {
       this.actionInfo = new WeChatAction(new WeChatScenerio(scenerioId, scenerioStr));
    }

    public class WeChatAction{

        @JsonProperty("scene")
        private WeChatScenerio scenerio;

        public WeChatScenerio getScenerio() {
            return scenerio;
        }

        public void setScenerio(WeChatScenerio scenerio) {
            this.scenerio = scenerio;
        }

        public WeChatAction(){}

        public WeChatAction(WeChatScenerio scenerio){
            this.scenerio = scenerio;
        }
    }

    public class WeChatScenerio{

        @JsonProperty("scene_id")
        private String sceniroId;

        @JsonProperty("scene_str")
        private String sceneStr;

        public String getSceniroId() {
            return sceniroId;
        }

        public void setSceniroId(String sceniroId) {
            this.sceniroId = sceniroId;
        }

        public String getSceneStr() {
            return sceneStr;
        }

        public void setSceneStr(String sceneStr) {
            this.sceneStr = sceneStr;
        }

        public WeChatScenerio(){}

        public WeChatScenerio(String scenerioId, String scenerioStr){
            this.setSceniroId(scenerioId);
            this.setSceneStr(scenerioStr);
        }
    }
}
