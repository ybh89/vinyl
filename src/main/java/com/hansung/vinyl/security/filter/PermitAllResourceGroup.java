package com.hansung.vinyl.security.filter;

import com.hansung.vinyl.authority.domain.Resource;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hansung.vinyl.authority.domain.HttpMethod.*;

@Getter
public class PermitAllResourceGroup {
    private static List<Resource> permitAllResources = new ArrayList<>();

    static {
        Resource loginResource = new Resource("/login", POST);
        Resource joinResource = new Resource("/*/accounts", POST);
        Resource emailResource = new Resource("/*/accounts/verify-email", GET);
        Resource catalogResource = new Resource("/*/news/**", GET);
        Resource docsResource = new Resource("/docs/**", GET);
        Resource identificationPostResource = new Resource("/*/identifications/**", POST);
        Resource identificationGetResource = new Resource("/*/identifications/**", GET);
        permitAllResources.addAll(Arrays.asList(loginResource, joinResource, catalogResource, docsResource,
                emailResource, identificationGetResource, identificationPostResource));
    }

    public static List<Resource> values() {
        return permitAllResources;
    }
}
