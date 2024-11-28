"use weex:vue";

if (typeof Promise !== 'undefined' && !Promise.prototype.finally) {
  Promise.prototype.finally = function(callback) {
    const promise = this.constructor
    return this.then(
      value => promise.resolve(callback()).then(() => value),
      reason => promise.resolve(callback()).then(() => {
        throw reason
      })
    )
  }
};

if (typeof uni !== 'undefined' && uni && uni.requireGlobal) {
  const global = uni.requireGlobal()
  ArrayBuffer = global.ArrayBuffer
  Int8Array = global.Int8Array
  Uint8Array = global.Uint8Array
  Uint8ClampedArray = global.Uint8ClampedArray
  Int16Array = global.Int16Array
  Uint16Array = global.Uint16Array
  Int32Array = global.Int32Array
  Uint32Array = global.Uint32Array
  Float32Array = global.Float32Array
  Float64Array = global.Float64Array
  BigInt64Array = global.BigInt64Array
  BigUint64Array = global.BigUint64Array
};


(()=>{var z=Object.create;var y=Object.defineProperty;var B=Object.getOwnPropertyDescriptor;var U=Object.getOwnPropertyNames;var q=Object.getPrototypeOf,H=Object.prototype.hasOwnProperty;var $=(e,t)=>()=>(t||e((t={exports:{}}).exports,t),t.exports);var Y=(e,t,i,r)=>{if(t&&typeof t=="object"||typeof t=="function")for(let l of U(t))!H.call(e,l)&&l!==i&&y(e,l,{get:()=>t[l],enumerable:!(r=B(t,l))||r.enumerable});return e};var w=(e,t,i)=>(i=e!=null?z(q(e)):{},Y(t||!e||!e.__esModule?y(i,"default",{value:e,enumerable:!0}):i,e));var d=$((ne,A)=>{A.exports=Vue});var re=w(d());function h(e,t,...i){uni.__log__?uni.__log__(e,t,...i):console[e].apply(console,[...i,t])}var D=(e,t)=>{let i=e.__vccOpts||e;for(let[r,l]of t)i[r]=l;return i};function S(e){return typeof e=="undefined"||e==null||e==""}var ae=new Promise(()=>{});function C(){return getApp().globalData.pluginTool.deviceID()}function k(){return getApp().globalData.pluginTool.vpnConnected()}function _(){return getApp().globalData.pluginTool.proxyOpened()}function P(){return getApp().globalData.pluginTool.carrierInfo()}function T(){return new Promise(e=>{let t=[];getApp().globalData.pluginTool.checkVirtualApp({list:t},i=>{e(i)})})}function x(){return typeof getApp().globalData.pluginTool.getApplnfo=="function"?getApp().globalData.pluginTool.getApplnfo():null}function F(e){typeof getApp().globalData.pluginTool.showFileDocumentView=="function"&&getApp().globalData.pluginTool.showFileDocumentView({},t=>{typeof e=="function"&&e(t.file_list)})}function I(e,t,i,r,l,n){typeof getApp().globalData.pluginTool.showSmallPopWebView=="function"&&getApp().globalData.pluginTool.showSmallPopWebView({htmlContent:e,frame:{x:r,y:l,w:t,h:i}},a=>{let g={},f=a.action.split("?")[1];if(!S(f)){let c=f.split("&");for(let m=0,G=c.length;m<G;m++){let v=c[m].split("=");g[v[0]]=v[1]}}Q(),typeof n=="function"&&n(g)})}function Q(){typeof getApp().globalData.pluginTool.dismissSmallPopView=="function"&&getApp().globalData.pluginTool.dismissSmallPopView()}function V(){if(typeof getApp().globalData.pluginTool.getDiskInfo=="function")return getApp().globalData.pluginTool.getDiskInfo()}function O(e){typeof getApp().globalData.pluginTool.getClipboardData=="function"&&getApp().globalData.pluginTool.getClipboardData(t=>{typeof e=="function"&&e(t)})}function E(){typeof getApp().globalData.pluginTool.dismissSmallPopView=="function"&&getApp().globalData.pluginTool.dismissSmallPopView()}function N(e,t){typeof getApp().globalData.pluginTool.getCurrentLocation=="function"&&getApp().globalData.pluginTool.getCurrentLocation({priority:e},i=>{typeof t=="function"&&t(i)})}function L(e){typeof getApp().globalData.pluginTool.permissionOpenSetting=="function"&&getApp().globalData.pluginTool.permissionOpenSetting({permission:e})}function J(e){if(typeof getApp().globalData.pluginTool.permissionStatusSync=="function")return getApp().globalData.pluginTool.permissionStatusSync({permission:e})}function M(e,t){typeof getApp().globalData.pluginTool.permissionRequest=="function"&&getApp().globalData.pluginTool.permissionRequest({permission:e},i=>{typeof t=="function"&&t(i)})}function R(e,t,i){if(typeof getApp().globalData.pluginTool.getFilePath=="function")return getApp().globalData.pluginTool.getFilePath({file_name:e,doc:t,is_vue:i})}var j={zT:2,appId:plus.runtime.appid,appV:parseInt(plus.runtime.version.split(".").join("")),isAndroid:plus.os.name=="Android"};function s(e,t,i,r="Confirm"){p(e,t,i,"",r,!1)}function p(e,t,i,r,l="Confirm",n=!0,a="Cancel",g=!1,f){uni.showModal({title:e,content:t,showCancel:n,cancelText:a,confirmText:l,editable:g,placeholderText:f,success(c){c.confirm?typeof i=="function"&&i(c):c.cancel&&typeof r=="function"&&r()}})}function W(e){let t=getApp().globalData.ossPicPath;return`
        <!DOCTYPE html>
		<html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
            <title>reception-notify</title>
            <style>
                html,body {
                    padding: 0;
                    margin: 0;
                    font-family: system-ui, sans-serif;
                    background: rgba(0,0,0,0);
                    overscroll-behavior-y: none;
                }
                .center {
                    display: flex;
                    align-items: center;
                }
                .reception-alert {
                    width: 6.04rem;
                    height: 4.46rem;
                    border-radius: 0.16rem;
                    background-color: rgba(68, 68, 68, 0.9);
                    box-shadow: 0 0.04rem 0.08rem 0 rgba(0,0,0,0.5);
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    justify-content: center;
                    position: absolute;
                    left: 50%;
                    top: -4.6rem;
                    transform: translateX(-50%);
                }
                .reception-transition {
                    transition: top ease 0.5s;
                }
                .reception-open {
                    top: 0.56rem;
                }
                .close-alert {
                    width: 0.26rem;
                    height: 0.26rem;
                    position: absolute;
                    top: 0.2rem;
                    right: 0.2rem;
                }
                .close-alert::after {
                    content: '';
                    width: 0.02rem;
                    height: 0.26rem;
                    border-radius: 0.02rem;
                    background-color: #5E6060;
                    transform: rotate(45deg);
                    position: absolute;
                    top: 0;
                    left: 0.12rem;
                }
                .close-alert::before {
                    content: '';
                    width: 0.02rem;
                    height: 0.26rem;
                    border-radius: 0.02rem;
                    background-color: #5E6060;
                    transform: rotate(-45deg);
                    position: absolute;
                    top: 0;
                    left: 0.12rem;
                }
                .recp-head {
                    width: 1.2rem;
                    height: 1.2rem;
                    margin-bottom: 0.24rem;
                    position: relative;
                }
                .head-img {
                    width: 1.2rem;
                    height: 1.2rem;
                    object-fit: cover;
                    border-radius: 0.6rem;
                }
                .gold-money {
                    width: 1.16rem;
                    height: 0.28rem;
                    border-radius: 0.08rem;
                    box-sizing: border-box;
                    background-color: #565656;
                    position: absolute;
                    left: 0;
                    bottom: -0.04rem;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                }
                .gold-money-text {
                    font-size: 0.16rem;
                    color: #FFFFFF;
                }
                .ellipsis {
                    max-width: 3rem;
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    font-size: 0.34rem;
                    color: #ffffff;
                    font-weight: 600;
                    margin-right: 0.1rem;
                    box-sizing: border-box;
                    padding-bottom: 0.04rem;
                }
                .sex-age {
                    height: 0.24rem;
                    box-sizing: border-box;
                    border: 0.01rem solid rgba(255, 255, 255, 0.5);
                    border-radius: 0.08rem;
                    padding: 0 0.08rem 0 0.04rem;
                }
                .age-text {
                    font-size: 0.16rem;
                    font-weight: 500;
                    color: #ffffff;
                }
                .level-view {
                    height: 0.24rem;
                    border-radius: 0 0.08rem 0.08rem 0;
                    border: 0.01rem solid #FA8F00;
                    position: relative;
                    box-sizing: border-box;
                    padding-right: 0.08rem;
                    padding-left: 0.2rem;
                    margin-left: 0.22rem;
                }
                .level-text {
                    font-size: 0.16rem;
                    font-weight: 500;
                    color: #FA8F00;
                }
                .level-icon {
                    width: 0.32rem;
                    height: 0.32rem;
                    position: absolute;
                    left: -0.16rem;
                    top: -0.04rem;
                }
                .country-icon {
                    width: 0.24rem;
                    height: 0.24rem;
                    border-radius: 0.08rem;
                    border: 0.01rem solid rgba(0,0,0,0.2);
                    object-fit: cover;
                    margin-left: 0.08rem;
                }
                .income-call {
                    margin-top: 0.12rem;
                    margin-bottom: 0.24rem;
                }
                .speak-language {
                    color: rgba(255,255,255,0.8);
                    font-size: 0.2rem;
                }
                .same {
                    color: #FFFFFF;
                }
                .invite-call {
                    color: #FFFFFF;
                    font-size: 0.24rem;
                    margin-bottom: 0.4rem;
                }
                .recp-btn {
                    width: 5.48rem;
                    height: 0.64rem;
                    display: flex;
                    flex-direction: row;
                    align-items: center;
                    justify-content: center;
                    border-radius: 0.12rem;
                    background-color: #34C759;
                }
                .recp-btn-text {
                    font-size: 0.24rem;
                    color: #ffffff;
                }
            </style>
            <script type="text/javascript">
                (function (win, doc) {
                    if (!win.addEventListener) return;
                    var html = document.documentElement;
        
                    function setFont() {
                        var html = document.documentElement;
                        var k = 750;
                        html.style.fontSize = html.clientWidth / k * 100 + "px";
                    }
        
                    setFont();
                    setTimeout(function () {
                        setFont();
                    }, 300);
                    doc.addEventListener('DOMContentLoaded', setFont, false);
                    win.addEventListener('resize', setFont, false);
                    win.addEventListener('load', setFont, false);
                })(window, document);
            <\/script>
        </head>
        <body>
        <div class="reception-alert">
            <div onclick="closeAlert()" class="close-alert"></div>
            <div class="recp-head">
                <img src="${t+getApp().pubUser.small_avatar}" class="head-img">
                <div class="gold-money">
                    <img src="${t}s-cupid/match/income_video.png" style="width: 0.24rem;height: 0.24rem;margin-right: 0.02rem;" />
                    <span class="gold-money-text">100 coins</span>
                    <img src="${t}s-cupid/match/gold_icon.png" style="width: 0.16rem;height: 0.16rem;" />
                    <span class="gold-money-text">Min - ${e||""}</span>
                </div>
            </div>
            <div class="center">
                <div class="ellipsis">${getApp().pubUser.nickname}</div>
                <div class="sex-age center">
                    <span class="age-text">20</span>
                </div>
                <div class="level-view center">
                    <span class="level-text">18</span>
                    <img class="level-icon" src="${t}s-cupid/pub/level_crown.png" />
                </div>
            </div>
            <div onclick="inviteCall()" class="recp-btn">
                <img src="${t}s-cupid/match/reception_video.png" style="width: 0.36rem;height: 0.36rem;margin-right: 0.12rem;" />
                <span class="recp-btn-text">Accpet</span>
            </div>
        </div>
        
        <script type="text/javascript">
            let receptionAlert = document.getElementsByClassName('reception-alert')[0]
            let levelView = document.getElementsByClassName('level-view')[0]
            let levelStr = levelView.firstElementChild.innerText
            let timer = null
            
            if (parseInt(levelStr) <= 0) {
                levelView.remove()
            }
            
            setTimeout(() => {
                receptionAlert.classList.add('reception-transition', 'reception-open')
                timeClose()
            }, 500)
            
            function timeClose() {
                clearTimer()
                timer = setTimeout(() => {
                    closeAlert()
                }, 5000)
            }
            
            function clearTimer() {
                if (timer) {
                    clearTimeout(timer)
                    timer = null
                }
            }
        
            function closeAlert() {
                clearTimer()
                receptionAlert.classList.remove('reception-open')
                setTimeout(() => {
                    location.href = 'https://jsbridge/close'
                }, 500)
            }
        
            function inviteCall() {
                clearTimer()
                receptionAlert.classList.remove('reception-open')
                setTimeout(() => {
                    location.href = 'https://jsbridge/acceptCall?invite=1'
                }, 500)
            }
        <\/script>
        </body>
        </html>
    `}var o=w(d());function X(){if(!j.isAndroid)return plus.navigator.isRoot()}function K(){return plus.navigator.isSimulator()}function Z(){let e=_(),t=k(),i=P(),r=X(),l=K(),n={};return typeof e!="undefined"&&(n.has_proxy=e?1:0),typeof t!="undefined"&&(n.has_vpn=t?1:0),typeof i!="undefined"&&(n.operator=i),typeof r!="undefined"&&(n.is_root=r?1:0),typeof l!="undefined"&&(n.is_simulator=l?1:0),n}var ee={"input-view":{"":{marginTop:"20rpx",width:200,height:44,lineHeight:44,color:"#333333",borderWidth:1,borderStyle:"solid",borderColor:"#FF4477"}}},te={data(){return{imageBase64:""}},methods:{clickGetDiskInfo(){let e=V();p("\u78C1\u76D8\u4FE1\u606F",JSON.stringify(e))},clickGetClipboardData(){O(e=>{p("\u7C98\u8D34\u677F",JSON.stringify(e))})},clickShowSmallPopWebView(){let e=W(),t=getApp().globalData.window.width,i=uni.upx2px(512)+getApp().globalData.window.statusBarHeight;I(e,t,i,0,0,r=>{p("\u7C98\u8D34\u677F",r)})},clickDismissSmallPopView(){E()},clickGetCurrentLocation(){N(100,e=>{s("location",JSON.stringify(e))})},clickGetAppInfo(){let e=x();s("location",JSON.stringify(e))},clickGetDeviceId(){let e=C();s("DeviceId",e)},clickGetDeviceInfo(){let e=Z();s("DeviceInfo",JSON.stringify(e))},clickCheckVirtualApp(){T().then(e=>{s("VirtualApp Success",JSON.stringify(e))}).catch(e=>{s("VirtualApp Error",JSON.stringify(e))})},clickPermissionOpenSetting(){L(["android.permission.READ_MEDIA_IMAGES","android.permission.READ_MEDIA_VIDEO"])},clickPermissionStatusSync(){let e=J(["android.permission.READ_MEDIA_IMAGES","android.permission.READ_MEDIA_VIDEO"]);s("Permission","\u662F\u5426\u83B7\u5F97\uFF1A"+e)},clickPermissionRequest(){M(["android.permission.READ_MEDIA_IMAGES","android.permission.READ_MEDIA_VIDEO"],e=>{s("Permission","\u662F\u5426\u83B7\u5F97\uFF1A"+JSON.stringify(e))})},clickGetFilePath(){let e=R("seagull1.png","image",!1);s("FilePath",e)},openFileSelector(e){F(t=>{let i=t[0],r="";if(e==0)r="\u9274\u9EC4";else if(e==1)r="\u4EBA\u8138";else if(e==2){if(r="\u5F62\u4F3C\u5EA6",t.length>=2){let l=t[0].path,n=t[1].path;faceCompare(l,n,a=>{s("\u662F\u5426\u540C\u4E00\u4EBA>0.8",JSON.stringify(a))})}else s("\u9274\u9EC4","\u8BF7\u9009\u62E9\u4E24\u5F20\u7167\u7247");return}p("\u56FE\u7247\u4FE1\u606F",i.path,()=>{e==0?getNSFWScore(i.path,l=>{h("log","at pagesPub/tool-view.nvue:185",JSON.stringify(l)),s("\u9274\u9EC4",JSON.stringify(l))}):e==1&&faceDetector(i.path,l=>{s("\u4EBA\u8138",JSON.stringify(l))})},"",r)})},clickCancelPreCacheVideo(){}}};function oe(e,t,i,r,l,n){let a=(0,o.resolveComponent)("button");return(0,o.openBlock)(),(0,o.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,o.createElementVNode)("div",{style:{flex:"1"}},[(0,o.createVNode)(a,{onClick:n.clickGetCurrentLocation},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u5730\u7406\u4F4D\u7F6E")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickGetAppInfo},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("APP Info")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickGetDeviceId},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("Device Id")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickGetDeviceInfo},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u8BBE\u5907\u4FE1\u606F")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickCheckVirtualApp},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u662F\u5426\u7834\u89E3")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickPermissionStatusSync},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u6743\u9650\u68C0\u6D4B(\u56FE\u7247\u3001\u89C6\u9891)")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickPermissionRequest},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u83B7\u53D6\u6743\u9650(\u56FE\u7247\u3001\u89C6\u9891)")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickPermissionOpenSetting},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u6253\u5F00\u6743\u9650\u8BBE\u7F6E(\u56FE\u7247\u3001\u89C6\u9891)")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickGetDiskInfo},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u83B7\u53D6\u78C1\u76D8\u4FE1\u606F")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickGetClipboardData},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u83B7\u53D6\u7C98\u8D34\u677F")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickShowSmallPopWebView},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u5F39\u51FA\u6846(\u5185\u5D4Cweb\u9875\u9762\u6837\u5F0F)")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickDismissSmallPopView},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u5173\u95ED\u5F39\u51FA\u6846")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:n.clickGetFilePath},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u83B7\u53D6\u56FE\u7247\u5730\u5740")]),_:1},8,["onClick"])])])}var u=D(te,[["render",oe],["styles",[ee]]]);var b=plus.webview.currentWebview();if(b){let e=parseInt(b.id),t="pagesPub/tool-view",i={};try{i=JSON.parse(b.__query__)}catch(l){}u.mpType="page";let r=Vue.createPageApp(u,{$store:getApp({allowDefault:!0}).$store,__pageId:e,__pagePath:t,__pageQuery:i});r.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...u.styles||[]])),r.mount("#root")}})();
