package com.liyawa.elasticsearchboot.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSiteInfo {
  public static final String DEFAULT_THEME = "standard";

  private long id;

  private long uid;

  private Long delegateUserId;

  private String siteTitle;

  private String siteTagline;

  private String logoUrl;

  private String bottomLogoUrl;

  private int templateId;

  private int styleId;

  private String domainName;

  private int status;

  private String backgroundPicture;

  private String backgroundPictureType;

  private String videoUrl;

  private String preVideoPicture;

  private long parentId;

  private long deadLine;

  private int agentSiteType;

  private String template;

  private List<WebSiteInfo> subdomains = new ArrayList<>();
  private String tag;
  private String titleLogo;

  private int isPlayVideo;

  // At the request of front-end, included, this field is placed here
  private boolean videoMuted;// Whether the play, not play by default

  private int showProgressBar;
  private boolean isVow;

  private boolean broker;

  private boolean isNewVersion;

  private boolean searchCenter;

  private int evaluationSite;

  private boolean heSite;
  private boolean idxFromEvaluationSite;

  private int siteLevel;
  private int siteType;

  private Date createTime;

  private Date updateTime;

  private boolean hasWebPackage = false;

  private String owner;
}
