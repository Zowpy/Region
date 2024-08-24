package me.zowpy.region.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FlagAccess {

    EVERYONE("Everyone"),
    WHITELIST("Whitelist"),
    NONE("None");

    private final String displayName;
}
