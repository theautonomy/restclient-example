package com.example.restclientdemo.client;

import java.util.Map;

import com.example.restclientdemo.model.HttpBinResponse;
import com.example.restclientdemo.model.SearchQuery;
import com.example.restclientdemo.model.User;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface HttpBinClient {

    @GetExchange("/get")
    HttpBinResponse get();

    @GetExchange("/get")
    HttpBinResponse getWithParams(@RequestParam Map<String, String> params);

    @PostExchange("/post")
    HttpBinResponse post(@RequestBody User user);

    @GetExchange("/basic-auth/{user}/{passwd}")
    HttpBinResponse basicAuth(
            @PathVariable String user,
            @PathVariable String passwd,
            @RequestHeader("Authorization") String authorization);

    @GetExchange("/get")
    HttpBinResponse search(SearchQuery searchQuery);
}
