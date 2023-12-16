package com.mzalcmanis.transfer.service;

import com.mzalcmanis.transfer.api.ApiResult;
import com.mzalcmanis.transfer.dto.Account;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    ApiResult<List<Account>> getClientAccounts(UUID clientId);
}
