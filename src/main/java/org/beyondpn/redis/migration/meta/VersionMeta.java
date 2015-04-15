package org.beyondpn.redis.migration.meta;

import java.util.Date;

/**
 * Created by yangjianhua on 2015/4/15.
 */
public class VersionMeta {

    private int version;
    private String script;
    private String description;
    private long hash;
    private Date executeOn;
    private int executeMills;
    private int success; //0:not success;1:success

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getHash() {
        return hash;
    }

    public void setHash(long hash) {
        this.hash = hash;
    }

    public Date getExecuteOn() {
        return executeOn;
    }

    public void setExecuteOn(Date executeOn) {
        this.executeOn = executeOn;
    }

    public int getExecuteMills() {
        return executeMills;
    }

    public void setExecuteMills(int executeMills) {
        this.executeMills = executeMills;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
