package com.homedepot.mm.pc.merchantalerting.template;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;

@Data
public class DefaultTemplate {
    String title;
    String titleDescription;
    String primaryText1;
    String primaryText2;
    String tertiaryText;
    String primaryLinkText;
    String primaryLinkUri;

    public JSONObject toJSONObject(DefaultTemplate template) throws Exception
    { JSONObject newJSON = new JSONObject();

        this.title=template.getTitle();
        this.titleDescription=template.getTitleDescription();
        newJSON.put(title,titleDescription);

        this.primaryText1=template.getPrimaryText1();
        this.primaryText2=template.getPrimaryText2();
        newJSON.put(primaryText1,primaryText2);

        this.tertiaryText=template.getTertiaryText();
        newJSON.put(tertiaryText,null);

        this.primaryLinkText=template.getPrimaryLinkText();
        this.primaryLinkUri=template.getPrimaryLinkUri();
        newJSON.put(primaryLinkText,primaryLinkUri);
        return newJSON;
    }

}