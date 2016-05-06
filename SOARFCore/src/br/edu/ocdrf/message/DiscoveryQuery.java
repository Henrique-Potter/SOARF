package br.edu.ocdrf.message;

import com.fasterxml.jackson.databind.JavaType;

public class DiscoveryQuery extends CryptoMessages<QueryData> {

    public String queryData;

    public void setQueryDataAndEncrypt(QueryData qData, String sessionKey) throws Exception {

        queryData = mapDataAsJsonAndEncrypt(qData, sessionKey,false);

    }

    public QueryData getQueryData(String sessionKey) throws Exception {

        JavaType type = mapper.getTypeFactory().constructType(QueryData.class);
        QueryData qData = getDataField(sessionKey, queryData, type, false);

        return qData;
    }

}
