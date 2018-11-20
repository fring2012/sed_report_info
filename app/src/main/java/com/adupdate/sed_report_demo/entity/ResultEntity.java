package com.adupdate.sed_report_demo.entity;

import com.adupdate.sed_report_demo.http.ResultBody;
import com.google.gson.annotations.SerializedName;

public class ResultEntity {
    @SerializedName("seqno")
    private String seqno;
    @SerializedName("replyno")
    private String replyno;
    @SerializedName("body")
    private ResultBody body;

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getReplyno() {
        return replyno;
    }

    public void setReplyno(String replyno) {
        this.replyno = replyno;
    }

    public ResultBody getBody() {
        return body;
    }

    public void setBody(ResultBody body) {
        this.body = body;
    }
}
