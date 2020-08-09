package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.request.WalletNgTransferRequest;
import okhttp3.Response;
import org.springframework.stereotype.Service;

@Service
public interface WalletsAfricaService {

    String getBanks();

    Response transferToBank(WalletNgTransferRequest transferRequest);

}
