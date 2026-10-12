package com.nttdata.orderflow.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExternalLoginResponse(@JsonProperty("access_token") String accessToken) {
}
