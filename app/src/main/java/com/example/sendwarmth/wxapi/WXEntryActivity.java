package com.example.sendwarmth.wxapi;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sendwarmth.util.LogUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("WXEntryActivity", "onCreate");
        Toast.makeText(this, "WXEntrayActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtil.e("WXEntryActivity", "onPayFinish, errCode = " + baseResp.errCode);
        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "不支持错误";
                break;
            default:
                result = "发送返回";
                break;
        }

        Toast.makeText(this, result + ", type=" + baseResp.getType(), Toast.LENGTH_SHORT).show();

        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    //这里肯定不能是像上面的DEMO一样弹出对话框了，而是通知我们发起支付调用的页面
                    //然后及时finish掉这个页面，贴个伪代码：
//            sendPayNotice()；
            finish();
        }
    }
}
