package com.nassaty.hireme.utils;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.pchmn.materialchips.model.ChipInterface;

// FIXME: 8/13/2018 find some place to tag people or something thx bdw.
public class TagChip implements ChipInterface {

    private String id;
    private Uri avatarUri;
    private String name;
    private String description;

    public TagChip(String id, Uri avatarUri, String name, String description) {
        this.id = id;
        this.avatarUri = avatarUri;
        this.name = name;
        this.description = description;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Uri getAvatarUri() {
        return avatarUri;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String getInfo() {
        return description;
    }
}
