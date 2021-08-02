package com.hansung.vinyl.security.domain;

import com.hansung.vinyl.authority.domain.Resource;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hansung.vinyl.authority.domain.HttpMethod.GET;
import static com.hansung.vinyl.authority.domain.HttpMethod.POST;

@Getter
public class PermitAllResourceGroup {
    private static List<Resource> permitAllResources = new ArrayList<>();

    static {
        Resource loginResource = new Resource("/login", POST);
        Resource joinResource = new Resource("/accounts", POST);
        Resource catalogResource = new Resource("/news/**", GET);
        permitAllResources.addAll(Arrays.asList(loginResource, joinResource, catalogResource));
    }

    public static List<Resource> values() {
        return permitAllResources;
    }
}
