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


(()=>{var me=Object.create;var R=Object.defineProperty;var de=Object.getOwnPropertyDescriptor;var he=Object.getOwnPropertyNames;var be=Object.getPrototypeOf,ve=Object.prototype.hasOwnProperty;var ye=(e,r)=>()=>(r||e((r={exports:{}}).exports,r),r.exports);var Ae=(e,r,t,o)=>{if(r&&typeof r=="object"||typeof r=="function")for(let n of he(r))!ve.call(e,n)&&n!==t&&R(e,n,{get:()=>r[n],enumerable:!(o=de(r,n))||o.enumerable});return e};var E=(e,r,t)=>(t=e!=null?me(be(e)):{},Ae(r||!e||!e.__esModule?R(t,"default",{value:e,enumerable:!0}):t,e));var W=(e,r,t)=>new Promise((o,n)=>{var a=p=>{try{s(t.next(p))}catch(c){n(c)}},i=p=>{try{s(t.throw(p))}catch(c){n(c)}},s=p=>p.done?o(p.value):Promise.resolve(p.value).then(a,i);s((t=t.apply(e,r)).next())});var k=ye((xe,B)=>{B.exports=Vue});var $e=E(k());function b(e,r,...t){uni.__log__?uni.__log__(e,r,...t):console[e].apply(console,[...t,r])}var J=(e,r)=>{let t=e.__vccOpts||e;for(let[o,n]of r)t[o]=n;return t};function v(e){return typeof e=="undefined"||e==null||e==""}var D=new Promise(()=>{});function z(e){return encodeURIComponent(e).replace(/[!'()*]/g,function(r){return"%"+r.charCodeAt(0).toString(16).toUpperCase()})}function q(e){try{let r=JSON.parse(e);if(typeof r=="object")return r}catch(r){}return!1}function H(e){if(!e)return"";let r=JSON.stringify(e);return(typeof r!="string"||r=="{}")&&(r=e.toString()),r}function K(e,r,t=""){if(v(e))return"";let o=[];for(let n in e)o.push(n+"="+e[n]);return t+o.join(r)}function y(e,r=null,t=null,o=!1){let n=Y(e,r),a={url:n,fail:i=>{b("error","at common/route.js:12",`\u6253\u5F00\u9875\u9762\u5931\u8D25${n} ${JSON.stringify(i)}`)}};o&&(a=Object.assign(a,o)),t!=null&&(a.events=t),uni.navigateTo(a)}function Q(e,r,t,o,n=null){let i={url:Y(e,r),success(s){s.eventChannel.emit(t,o)}};n!=null&&(i.events=n),uni.navigateTo(i)}function Y(e,r){return e+K(r,"&","?")}function G(e){if(typeof getApp().globalData.pluginTool.saveScreenShotPhotoAndVideo!="function")throw"\u627E\u4E0D\u5230\u63D2\u4EF6";let r={url:plus.io.convertLocalFileSystemURL("_doc")};getApp().globalData.pluginTool.saveScreenShotPhotoAndVideo(r,t=>{e(t)})}function X(){typeof getApp().globalData.pluginTool.stopSaveScreenShotMonitor=="function"&&getApp().globalData.pluginTool.stopSaveScreenShotMonitor({},()=>{})}function Z(e){typeof getApp().globalData.pluginTool.showFileDocumentView=="function"&&getApp().globalData.pluginTool.showFileDocumentView({},r=>{typeof e=="function"&&e(r.file_list)})}function ee(e,r,t,o,n,a){typeof getApp().globalData.pluginTool.showSmallPopWebView=="function"&&getApp().globalData.pluginTool.showSmallPopWebView({htmlContent:e,frame:{x:o,y:n,w:r,h:t}},i=>{let s={},p=i.action.split("?")[1];if(!v(p)){let c=p.split("&");for(let A=0,w=c.length;A<w;A++){let S=c[A].split("=");s[S[0]]=S[1]}}I(),typeof a=="function"&&a(s)})}function I(){typeof getApp().globalData.pluginTool.dismissSmallPopView=="function"&&getApp().globalData.pluginTool.dismissSmallPopView()}var we={zT:2,appId:plus.runtime.appid,appV:parseInt(plus.runtime.version.split(".").join("")),isAndroid:plus.os.name=="Android"};function te(e,r){getApp().globalData.s.commit("setPubValue",{name:e,value:r})}function oe(e,r="none",t="center"){uni.showToast({title:e,duration:2e3,icon:r,position:t})}function _(e,r,t,o="Confirm"){U(e,r,t,"",o,!1)}function U(e,r,t,o,n="Confirm",a=!0,i="Cancel",s=!1,p){uni.showModal({title:e,content:r,showCancel:a,cancelText:i,confirmText:n,editable:s,placeholderText:p,success(c){c.confirm?typeof t=="function"&&t(c):c.cancel&&typeof o=="function"&&o()}})}function ne(e,r){let t={mask:!!e};r&&(t.title=r),uni.showLoading(t)}function re(){uni.hideLoading()}var Ye=E(k());function ae(){plus.navigator.closeSplashscreen()}function ie(e){let r=getApp().globalData.ossPicPath;return`
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
                <img src="${r+getApp().pubUser.small_avatar}" class="head-img">
                <div class="gold-money">
                    <img src="${r}s-cupid/match/income_video.png" style="width: 0.24rem;height: 0.24rem;margin-right: 0.02rem;" />
                    <span class="gold-money-text">100 coins</span>
                    <img src="${r}s-cupid/match/gold_icon.png" style="width: 0.16rem;height: 0.16rem;" />
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
                    <img class="level-icon" src="${r}s-cupid/pub/level_crown.png" />
                </div>
            </div>
            <div onclick="inviteCall()" class="recp-btn">
                <img src="${r}s-cupid/match/reception_video.png" style="width: 0.36rem;height: 0.36rem;margin-right: 0.12rem;" />
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
    `}var V=0;function Ce(e){return De(Se(Pe(e)))}function Se(e){return _e(Fe(Te(e),e.length*8))}function De(e){try{}catch(a){V=0}for(var r=V?"0123456789ABCDEF":"0123456789abcdef",t="",o,n=0;n<e.length;n++)o=e.charCodeAt(n),t+=r.charAt(o>>>4&15)+r.charAt(o&15);return t}function Pe(e){for(var r="",t=-1,o,n;++t<e.length;)o=e.charCodeAt(t),n=t+1<e.length?e.charCodeAt(t+1):0,55296<=o&&o<=56319&&56320<=n&&n<=57343&&(o=65536+((o&1023)<<10)+(n&1023),t++),o<=127?r+=String.fromCharCode(o):o<=2047?r+=String.fromCharCode(192|o>>>6&31,128|o&63):o<=65535?r+=String.fromCharCode(224|o>>>12&15,128|o>>>6&63,128|o&63):o<=2097151&&(r+=String.fromCharCode(240|o>>>18&7,128|o>>>12&63,128|o>>>6&63,128|o&63));return r}function Te(e){for(var r=Array(e.length>>2),t=0;t<r.length;t++)r[t]=0;for(var t=0;t<e.length*8;t+=8)r[t>>5]|=(e.charCodeAt(t/8)&255)<<t%32;return r}function _e(e){for(var r="",t=0;t<e.length*32;t+=8)r+=String.fromCharCode(e[t>>5]>>>t%32&255);return r}function Fe(e,r){e[r>>5]|=128<<r%32,e[(r+64>>>9<<4)+14]=r;for(var t=1732584193,o=-271733879,n=-1732584194,a=271733878,i=0;i<e.length;i+=16){var s=t,p=o,c=n,A=a;t=g(t,o,n,a,e[i+0],7,-680876936),a=g(a,t,o,n,e[i+1],12,-389564586),n=g(n,a,t,o,e[i+2],17,606105819),o=g(o,n,a,t,e[i+3],22,-1044525330),t=g(t,o,n,a,e[i+4],7,-176418897),a=g(a,t,o,n,e[i+5],12,1200080426),n=g(n,a,t,o,e[i+6],17,-1473231341),o=g(o,n,a,t,e[i+7],22,-45705983),t=g(t,o,n,a,e[i+8],7,1770035416),a=g(a,t,o,n,e[i+9],12,-1958414417),n=g(n,a,t,o,e[i+10],17,-42063),o=g(o,n,a,t,e[i+11],22,-1990404162),t=g(t,o,n,a,e[i+12],7,1804603682),a=g(a,t,o,n,e[i+13],12,-40341101),n=g(n,a,t,o,e[i+14],17,-1502002290),o=g(o,n,a,t,e[i+15],22,1236535329),t=f(t,o,n,a,e[i+1],5,-165796510),a=f(a,t,o,n,e[i+6],9,-1069501632),n=f(n,a,t,o,e[i+11],14,643717713),o=f(o,n,a,t,e[i+0],20,-373897302),t=f(t,o,n,a,e[i+5],5,-701558691),a=f(a,t,o,n,e[i+10],9,38016083),n=f(n,a,t,o,e[i+15],14,-660478335),o=f(o,n,a,t,e[i+4],20,-405537848),t=f(t,o,n,a,e[i+9],5,568446438),a=f(a,t,o,n,e[i+14],9,-1019803690),n=f(n,a,t,o,e[i+3],14,-187363961),o=f(o,n,a,t,e[i+8],20,1163531501),t=f(t,o,n,a,e[i+13],5,-1444681467),a=f(a,t,o,n,e[i+2],9,-51403784),n=f(n,a,t,o,e[i+7],14,1735328473),o=f(o,n,a,t,e[i+12],20,-1926607734),t=m(t,o,n,a,e[i+5],4,-378558),a=m(a,t,o,n,e[i+8],11,-2022574463),n=m(n,a,t,o,e[i+11],16,1839030562),o=m(o,n,a,t,e[i+14],23,-35309556),t=m(t,o,n,a,e[i+1],4,-1530992060),a=m(a,t,o,n,e[i+4],11,1272893353),n=m(n,a,t,o,e[i+7],16,-155497632),o=m(o,n,a,t,e[i+10],23,-1094730640),t=m(t,o,n,a,e[i+13],4,681279174),a=m(a,t,o,n,e[i+0],11,-358537222),n=m(n,a,t,o,e[i+3],16,-722521979),o=m(o,n,a,t,e[i+6],23,76029189),t=m(t,o,n,a,e[i+9],4,-640364487),a=m(a,t,o,n,e[i+12],11,-421815835),n=m(n,a,t,o,e[i+15],16,530742520),o=m(o,n,a,t,e[i+2],23,-995338651),t=d(t,o,n,a,e[i+0],6,-198630844),a=d(a,t,o,n,e[i+7],10,1126891415),n=d(n,a,t,o,e[i+14],15,-1416354905),o=d(o,n,a,t,e[i+5],21,-57434055),t=d(t,o,n,a,e[i+12],6,1700485571),a=d(a,t,o,n,e[i+3],10,-1894986606),n=d(n,a,t,o,e[i+10],15,-1051523),o=d(o,n,a,t,e[i+1],21,-2054922799),t=d(t,o,n,a,e[i+8],6,1873313359),a=d(a,t,o,n,e[i+15],10,-30611744),n=d(n,a,t,o,e[i+6],15,-1560198380),o=d(o,n,a,t,e[i+13],21,1309151649),t=d(t,o,n,a,e[i+4],6,-145523070),a=d(a,t,o,n,e[i+11],10,-1120210379),n=d(n,a,t,o,e[i+2],15,718787259),o=d(o,n,a,t,e[i+9],21,-343485551),t=C(t,s),o=C(o,p),n=C(n,c),a=C(a,A)}return Array(t,o,n,a)}function L(e,r,t,o,n,a){return C(ke(C(C(r,e),C(o,a)),n),t)}function g(e,r,t,o,n,a,i){return L(r&t|~r&o,e,r,n,a,i)}function f(e,r,t,o,n,a,i){return L(r&o|t&~o,e,r,n,a,i)}function m(e,r,t,o,n,a,i){return L(r^t^o,e,r,n,a,i)}function d(e,r,t,o,n,a,i){return L(t^(r|~o),e,r,n,a,i)}function C(e,r){var t=(e&65535)+(r&65535),o=(e>>16)+(r>>16)+(t>>16);return o<<16|t&65535}function ke(e,r){return e<<r|e>>>32-r}var M={md5:function(e){return Ce(e)}};function Le(e,r){let t=getApp().globalData.s.state.AES.encrypt(r,e);return`${getApp().globalData.apiUaPrefix}${t}`}function le(e){return M.md5(`${getApp().globalData.channel}${e}${getApp().globalData.apiEncValue}`).substring(0,16)}var j={setToken(e){v(e)||te("pubToken",e)},isLogin(){let e=getApp().globalData.s.state;return!v(e.pubToken)&&e.pubUser.uid},request(e,r,t,o,n=!1){return new Promise((a,i)=>{var s,p,c;let A=!1;if(t)if(t=="refresh")A=!0;else{let h={mask:!0};typeof t=="string"&&(h.title=t),uni.showLoading(h)}let w=!1;try{w=getApp().globalData.s.state.pubToken}catch(h){b("error","at common/api.js:31","request token catch "+JSON.stringify(h))}v((s=getApp().globalData)==null?void 0:s.ua)&&(N(7,(p=getApp().globalData)==null?void 0:p.ua,typeof((c=getApp().globalData)==null?void 0:c.ua),r,e,w),getApp().initUaInfo());let S=getApp().globalData.ua,F,ue=Oe(r),P={"content-type":"application/json"},ge=getApp().globalData.apiHost,T;w&&getApp().globalData.uid!==0?(P.Authorization=`Bearer ${w}`,T=le(getApp().globalData.uid)):T=le(0),P.ua=Le(S,T),F=getApp().globalData.s.state.AES.encrypt(T,JSON.stringify(ue)),uni.request({url:ge+e,method:"POST",header:P,data:F,dataType:"json",timeout:1e4,complete(h){if(t&&uni.hideLoading(),b("log","at common/api.js:63",`request ${e} obj ${JSON.stringify(r)}`,h.statusCode),h.header&&h.header["x-report-log"]&&N(h.header["x-report-log"],P.ua,S,F,e,w),h.statusCode==200){let fe=h.data,u=getApp().globalData.s.state.AES.decrypt(T,fe);if(u=q(u),b("log","at common/api.js:71","res:",u),!u){o&&i(u),N(1,P.ua,S,F,e,w);return}if(u.data&&typeof u.data.callback=="object"&&(j.handleCallback(u.data.callback),n))return D;if(u.ok===99)return getApp().loginOut("API ok 99"),D;if(u.ok==-98)return o&&i(u),D;if(v(u.msg)||oe(u.msg),u.ok>0){a(u);return}o&&i(u)}else{if(h.statusCode==401)return getApp().loginOut("API status code 401"),D;if(A&&uni.stopPullDownRefresh(),o)i(h);else return D}},fail(h){}})})},handleCallback(e){},accessLogExtra(e,r,t){}};function Oe(e){try{v(e)&&(e={}),e.times=new Date().getTime();let r=Object.keys(e).sort(),t={};for(let n=0,a=r.length;n<a;n++)t[r[n]]=e[r[n]];let o=se(t).substr(1);return t.sign=M.md5(`${M.md5(o)}${getApp().globalData.apiSignValue}`),t}catch(r){return e}}function se(e,r){if(e==null||typeof e=="number"&&isNaN(e))return"";try{let t="",o=typeof e;if(o=="string"||o=="number")t+="&"+r+"="+z(e);else if(o=="boolean")t+="&"+r+"="+(e?1:0);else for(let n in e){let a=r==null?n:r+Ee(n);t+=se(e[n],a)}return t}catch(t){return""}}function Ee(e){return`${encodeURIComponent("[")}${e}${encodeURIComponent("]")}`}function N(e,r,t,o,n,a){let i={type:e,ua:r,origin_ua:t,params:o,api:n,token:a},s=!1;try{s=getApp().globalData.s.state.pubToken}catch(c){b("error","at common/api.js:182","request token catch "+JSON.stringify(c))}let p={"content-type":"application/json",ua:getApp().globalData.ua};s&&(p.Authorization=`Bearer ${s}`),uni.request({url:`${getApp().globalData.apiHost}/log/report`,method:"POST",header:p,data:i,dataType:"json",timeout:1e4})}var x={PHOTO:1,VIDEO:2,VOICE:3,TXT:4},pe={DB:"db",MESSAGE:"message",AVATAR:"avatar",LOG:"log",ALBUM:"album"};var Ie="/upload/params";function ce(e,r,t){return new Promise((o,n)=>W(this,null,function*(){let a=e.replace("file://",""),i={flag:r,type:t};try{let s=yield j.request(Ie,i,!1,!0),p=yield Ue(a,"",s.data);o(p)}catch(s){let p=H(s);b("error","at common/api-upload.js:27","upload media fail "+p),uni.hideLoading(),n(s)}}))}function Ue(e,r,t,o=0,n=0){return new Promise((a,i)=>{getApp().globalData.pluginUpload.singleUpS3({filePath:e,isBase64:o,coverFilePath:r,coverIsBase64:n,ossInfo:t},s=>{b("log","at common/api-upload.js:80","upload plugin s3 result",s),(typeof s=="object"?s.success:s)?a({path:t.dir.path,smallPath:t.dir.small_url}):i(`_upModuleS3 ${JSON.stringify(s)}`)})})}var l=E(k());var Ve={flex:{"":{flex:1}},center:{"":{flexDirection:"row",alignItems:"center"}}},Ne={data(){return{}},methods:{openAlbumSelectorView(){y("/pagesPub/album-selector")},openFileSelector(){Z(e=>{let r=e[0];U("Success",JSON.stringify(r),()=>{ne(!0),ce(r.path,pe.ALBUM,x.PHOTO).then(t=>{re(),_("\u4E0A\u4F20\u6210\u529F",t.path)})},"","\u6D4B\u8BD5\u4E0A\u4F20")})},openScreenshotMonitoring(){try{G(e=>{Q("/pagesPub/screenshot-monitoring-result",{},"screenshotData",e)}),_("Success",`\u5F00\u542F\u6210\u529F
\u8BF7\u64CD\u4F5C\u622A\u5C4F/\u5F55\u5C4F\uFF0C\u8FDB\u884C\u6D4B\u8BD5`)}catch(e){_("Error","\u5F00\u542F\u68C0\u6D4B\u5931\u8D25 "+e)}},cloeScreenshotMonitoring(){X(),_("Success","\u5173\u95ED \u622A\u56FE\u76D1\u6D4B")},clickOpenAppNotify(){let e=ie(),r=getApp().globalData.window.width,t=uni.upx2px(512)+getApp().globalData.window.statusBarHeight;ee(e,r,t,0,0)},clickCloseAppNotify(){I()},openCheckImageCache(){y("/pagesPub/image-cache")},openMqttSub(){y("/pagesPub/mqtt-sub")},openLottieView(){y("/pagesPub/lottie-view")},openToolView(){y("/pagesPub/tool-view")},openUserLog(){y("/pagesPub/user-log")},openUserRouteLog(){y("/pagesPub/user-route-log")},openAudio(){y("/pagesPub/audio-view")},openEvent(){y("/pagesPub/event-view")},opnePage(e){y(e)}},onLoad(){ae()},onUnload(){}};function Me(e,r,t,o,n,a){let i=(0,l.resolveComponent)("button");return(0,l.openBlock)(),(0,l.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,l.createElementVNode)("view",{style:{padding:"0 10rpx",flex:"1"}},[(0,l.createElementVNode)("list",{showScrollbar:!1,style:{flex:"1"}},[(0,l.createElementVNode)("cell",{class:"center"},[(0,l.createVNode)(i,{onClick:a.openScreenshotMonitoring,class:"flex"},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u5F00\u542F \u622A\u56FE\u76D1\u6D4B")]),_:1},8,["onClick"]),(0,l.createVNode)(i,{onClick:a.cloeScreenshotMonitoring,class:"flex"},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u5173\u95ED \u622A\u56FE\u76D1\u6D4B")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createElementVNode)("view",{class:"center",style:{"margin-top":"10rpx"}},[(0,l.createVNode)(i,{onClick:a.clickOpenAppNotify,class:"flex"},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u6253\u5F00 \u81EA\u5B9A\u4E49\u901A\u77E5\u680F")]),_:1},8,["onClick"]),(0,l.createVNode)(i,{onClick:a.clickCloseAppNotify,class:"flex"},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u5173\u95ED \u81EA\u5B9A\u4E49\u901A\u77E5\u680F")]),_:1},8,["onClick"])])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:a.openAudio,style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u97F3\u9891")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:a.openEvent,style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("event\u4E8B\u4EF6")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:a.openFileSelector,style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u6253\u5F00\u7CFB\u7EDF\u6587\u4EF6\u9009\u62E9\u5668")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:a.openAlbumSelectorView,style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u6253\u5F00\u76F8\u518C\u9009\u62E9View")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:a.openCheckImageCache,style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u56FE\u7247\u7F13\u5B58\u68C0\u6D4B")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:a.openMqttSub,style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("MQTT\u8BA2\u9605\u903B\u8F91")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:a.openLottieView,style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("Lottie View")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:a.openToolView,style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u5DE5\u5177\u6D4B\u8BD5")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:a.openUserLog,style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u7528\u6237\u65E5\u5FD7\u4FE1\u606F")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:a.openUserRouteLog,style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u7528\u6237\u8DEF\u7531\u4FE1\u606F")]),_:1},8,["onClick"])]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:r[0]||(r[0]=s=>a.opnePage("/pagesPub/adjust-view")),style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("adjust")]),_:1})]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:r[1]||(r[1]=s=>a.opnePage("/pagesPub/open-install-view")),style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("OpenInstall")]),_:1})]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:r[2]||(r[2]=s=>a.opnePage("/pagesPub/tflite-view")),style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("TFLite")]),_:1})]),(0,l.createElementVNode)("cell",null,[(0,l.createVNode)(i,{onClick:r[3]||(r[3]=s=>a.opnePage("/pagesPub/applet-view")),style:{"margin-top":"10rpx"}},{default:(0,l.withCtx)(()=>[(0,l.createTextVNode)("\u5C0F\u7A0B\u5E8F")]),_:1})])])])])}var O=J(Ne,[["render",Me],["styles",[Ve]]]);var $=plus.webview.currentWebview();if($){let e=parseInt($.id),r="pages/user/index",t={};try{t=JSON.parse($.__query__)}catch(n){}O.mpType="page";let o=Vue.createPageApp(O,{$store:getApp({allowDefault:!0}).$store,__pageId:e,__pagePath:r,__pageQuery:t});o.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...O.styles||[]])),o.mount("#root")}})();
