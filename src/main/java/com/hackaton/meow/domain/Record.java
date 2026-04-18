package com.hackaton.meow.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "event_up")
public class Record {

    @Id
    @Column(name = "deduplication_id")
    private UUID deduplicationId;

    @Column(name = "time")
    private OffsetDateTime time;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "application_id")
    private UUID applicationId;

    @Column(name = "application_name")
    private String applicationName;

    @Column(name = "device_profile_id")
    private UUID deviceProfileId;

    @Column(name = "device_profile_name")
    private String deviceProfileName;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "dev_eui", length = 16)
    private String devEui;

    @Column(name = "tags", columnDefinition = "jsonb")
    private String tags;

    @Column(name = "dev_addr", length = 8)
    private String devAddr;

    @Column(name = "adr")
    private Boolean adr;

    @Column(name = "dr")
    private Short dr;

    @Column(name = "f_cnt")
    private Long fCnt;

    @Column(name = "f_port")
    private Short fPort;

    @Column(name = "confirmed")
    private Boolean confirmed;

    @Column(name = "data")
    private byte[] data;

    @Column(name = "object", columnDefinition = "jsonb")
    private String object;

    @Column(name = "rx_info", columnDefinition = "jsonb")
    private String rxInfo;

    @Column(name = "tx_info", columnDefinition = "jsonb")
    private String txInfo;

    public UUID getDeduplicationId() {
        return deduplicationId;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public Boolean getAdr() {
        return adr;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public String getTenantName() {
        return tenantName;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public UUID getDeviceProfileId() {
        return deviceProfileId;
    }

    public String getDeviceProfileName() {
        return deviceProfileName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDevEui() {
        return devEui;
    }

    public String getTags() {
        return tags;
    }

    public String getDevAddr() {
        return devAddr;
    }

    public Short getDr() {
        return dr;
    }

    public Long getfCnt() {
        return fCnt;
    }

    public Short getfPort() {
        return fPort;
    }

    public byte[] getData() {
        return data;
    }

    public String getObject() {
        return object;
    }

    public String getRxInfo() {
        return rxInfo;
    }

    public String getTxInfo() {
        return txInfo;
    }
}
