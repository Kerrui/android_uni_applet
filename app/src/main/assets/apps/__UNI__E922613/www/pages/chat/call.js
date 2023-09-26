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


(()=>{var X=Object.create;var U=Object.defineProperty;var x=Object.getOwnPropertyDescriptor;var Z=Object.getOwnPropertyNames;var ee=Object.getPrototypeOf,te=Object.prototype.hasOwnProperty;var ie=(e,n)=>()=>(n||e((n={exports:{}}).exports,n),n.exports);var re=(e,n,t,i)=>{if(n&&typeof n=="object"||typeof n=="function")for(let r of Z(n))!te.call(e,r)&&r!==t&&U(e,r,{get:()=>n[r],enumerable:!(i=x(n,r))||i.enumerable});return e};var H=(e,n,t)=>(t=e!=null?X(ee(e)):{},re(n||!e||!e.__esModule?U(t,"default",{value:e,enumerable:!0}):t,e));var A=(e,n,t)=>new Promise((i,r)=>{var s=h=>{try{g(t.next(h))}catch(S){r(S)}},o=h=>{try{g(t.throw(h))}catch(S){r(S)}},g=h=>h.done?i(h.value):Promise.resolve(h.value).then(s,o);g((t=t.apply(e,n)).next())});var E=ie((He,B)=>{B.exports=Vue});var l=H(E());var L=(e,n)=>{let t=e.__vccOpts||e;for(let[i,r]of n)t[i]=r;return t};function C(e){return e==null||e==""||typeof e=="undefined"}function ne(e){return encodeURIComponent(e).replace(/[!'()*]/g,function(n){return"%"+n.charCodeAt(0).toString(16).toUpperCase()})}var T={};function W(e,n=500,t="throttle"){let i=e.name;C(i)&&(i=t),!T[i]&&(T[i]=!0,e(),setTimeout(function(){T[i]=""},n))}function se(){return Math.random().toString(36).substr(2)}var oe="dev",$,z,j,G;$="https://bts.267girl.com",z="https://pic3.candychat.link/",G="https://pic2.candychat.link/",j="1b28c2eac0672761";var _={wv:"1.0.2",ENV:oe,apiHost:$,isAndroid:plus.os.name=="Android",ossPicPath:z,ossPath:G,key:j,PHOTO:1,VIDEO:2,appV:parseInt(plus.runtime.version.split(".").join("")),M_TXT:1,M_IMG:3,M_VIDEO:4,M_VOICE:2,M_HUNG_UP:5};function ae(e){getApp().globalData.toolModule.permissionOpenSetting({permission:e})}function le(e){return new Promise(n=>{getApp().globalData.toolModule.permissionRequest({permission:e},t=>{n(t)})})}var u={ALBUM:1,CAMERA:2,VIDEO:3,MICROPHONE:4,ALBUM_AND_CAMERA:101},m={GRANTED:1,DENIED:0,DENIED_ALWAYS:-1};function ue(e){switch(e){case u.ALBUM:case u.VIDEO:return he;case u.MICROPHONE:return ge;case u.CAMERA:return ye}}function O(e,n=!0,t=!0){return new Promise(i=>{let r=function(s){s==m.GRANTED?i(!0):(s==m.DENIED||s==m.DENIED_ALWAYS&&t&&D(e),i(!1))};if(_.isAndroid){let s=q(e);le(s).then(r)}else ue(e)(!0).then(r)})}function q(e){switch(e){case u.ALBUM:case u.VIDEO:return["android.permission.READ_MEDIA_IMAGES","android.permission.READ_MEDIA_VIDEO"];case u.MICROPHONE:return["android.permission.RECORD_AUDIO"];case u.CAMERA:return["android.permission.CAMERA"]}}function ce(e){if(C(e)){de();return}ae(e)}function pe(e){if(_.isAndroid){let n=q(e);ce(n)}else me()}function D(e,n){uni.showModal({title:"Tips",content:fe(e),showCancel:!0,cancelText:"Cancel",confirmText:"To turn on",complete(t){t.confirm?(pe(e),typeof n=="function"&&n(!0)):t.cancel&&typeof n=="function"&&n(!1)}})}function fe(e){let n="App";switch(e){case u.ALBUM:case u.VIDEO:return`Turn on the read and write permissions for ${n} in the application management to use the album function normally`;case u.MICROPHONE:return`In the application management, enable the microphone permission for ${n} to use the voice function normally`;case u.CAMERA:return`In the application management, enable the camera permission for ${n} to use the camera function normally`}}function de(){var e=plus.android.importClass("android.content.Intent"),n=plus.android.importClass("android.provider.Settings"),t=plus.android.importClass("android.net.Uri"),i=plus.android.runtimeMainActivity(),r=new e;r.setAction(n.ACTION_APPLICATION_DETAILS_SETTINGS);var s=t.fromParts("package",i.getPackageName(),null);r.setData(s),i.startActivity(r)}function me(){var e=plus.ios.import("UIApplication"),n=e.sharedApplication(),t=plus.ios.import("NSURL"),i=t.URLWithString("app-settings:");n.openURL(i),plus.ios.deleteObject(i),plus.ios.deleteObject(t),plus.ios.deleteObject(n)}function he(){return new Promise(e=>{let n=plus.ios.import("PHPhotoLibrary"),t=n.authorizationStatus();t==3||t==4?(plus.ios.deleteObject(n),e(m.GRANTED)):t==0?plus.ios.invoke("PHPhotoLibrary","requestAuthorization:",function(){plus.ios.deleteObject(n),e(m.DENIED)}):(plus.ios.deleteObject(n),e(m.DENIED_ALWAYS))})}function ge(e){return new Promise(n=>{let t=plus.ios.importClass("AVCaptureDevice"),i=t.authorizationStatusForMediaType("soun");i==3?(plus.ios.deleteObject(t),n(m.GRANTED)):i==0?plus.ios.invoke("AVCaptureDevice","requestAccessForMediaType:completionHandler:","soun",function(){if(e){let r=t.authorizationStatusForMediaType("soun");plus.ios.deleteObject(t),n(r==3?m.GRANTED:m.DENIED)}else plus.ios.deleteObject(t),n(m.DENIED)}):(plus.ios.deleteObject(t),n(m.DENIED_ALWAYS))})}function ye(e){return new Promise(n=>{let t=plus.ios.importClass("AVCaptureDevice"),i=t.authorizationStatusForMediaType("vide");i==3?(plus.ios.deleteObject(t),n(m.GRANTED)):i==0?plus.ios.invoke("AVCaptureDevice","requestAccessForMediaType:completionHandler:","vide",function(){if(e){let r=t.authorizationStatusForMediaType("vide");plus.ios.deleteObject(t),n(r==3?m.GRANTED:m.DENIED)}else plus.ios.deleteObject(t),n(m.DENIED)}):(plus.ios.deleteObject(t),n(m.DENIED_ALWAYS))})}function N(e,n="none",t="center"){uni.showToast({title:e,duration:2e3,icon:n,position:t})}var Se=new Promise(()=>{});var M=0;function ve(e){return we(Ae(Ce(e)))}function Ae(e){return De(be(_e(e),e.length*8))}function we(e){try{}catch(s){M=0}for(var n=M?"0123456789ABCDEF":"0123456789abcdef",t="",i,r=0;r<e.length;r++)i=e.charCodeAt(r),t+=n.charAt(i>>>4&15)+n.charAt(i&15);return t}function Ce(e){for(var n="",t=-1,i,r;++t<e.length;)i=e.charCodeAt(t),r=t+1<e.length?e.charCodeAt(t+1):0,55296<=i&&i<=56319&&56320<=r&&r<=57343&&(i=65536+((i&1023)<<10)+(r&1023),t++),i<=127?n+=String.fromCharCode(i):i<=2047?n+=String.fromCharCode(192|i>>>6&31,128|i&63):i<=65535?n+=String.fromCharCode(224|i>>>12&15,128|i>>>6&63,128|i&63):i<=2097151&&(n+=String.fromCharCode(240|i>>>18&7,128|i>>>12&63,128|i>>>6&63,128|i&63));return n}function _e(e){for(var n=Array(e.length>>2),t=0;t<n.length;t++)n[t]=0;for(var t=0;t<e.length*8;t+=8)n[t>>5]|=(e.charCodeAt(t/8)&255)<<t%32;return n}function De(e){for(var n="",t=0;t<e.length*32;t+=8)n+=String.fromCharCode(e[t>>5]>>>t%32&255);return n}function be(e,n){e[n>>5]|=128<<n%32,e[(n+64>>>9<<4)+14]=n;for(var t=1732584193,i=-271733879,r=-1732584194,s=271733878,o=0;o<e.length;o+=16){var g=t,h=i,S=r,P=s;t=c(t,i,r,s,e[o+0],7,-680876936),s=c(s,t,i,r,e[o+1],12,-389564586),r=c(r,s,t,i,e[o+2],17,606105819),i=c(i,r,s,t,e[o+3],22,-1044525330),t=c(t,i,r,s,e[o+4],7,-176418897),s=c(s,t,i,r,e[o+5],12,1200080426),r=c(r,s,t,i,e[o+6],17,-1473231341),i=c(i,r,s,t,e[o+7],22,-45705983),t=c(t,i,r,s,e[o+8],7,1770035416),s=c(s,t,i,r,e[o+9],12,-1958414417),r=c(r,s,t,i,e[o+10],17,-42063),i=c(i,r,s,t,e[o+11],22,-1990404162),t=c(t,i,r,s,e[o+12],7,1804603682),s=c(s,t,i,r,e[o+13],12,-40341101),r=c(r,s,t,i,e[o+14],17,-1502002290),i=c(i,r,s,t,e[o+15],22,1236535329),t=p(t,i,r,s,e[o+1],5,-165796510),s=p(s,t,i,r,e[o+6],9,-1069501632),r=p(r,s,t,i,e[o+11],14,643717713),i=p(i,r,s,t,e[o+0],20,-373897302),t=p(t,i,r,s,e[o+5],5,-701558691),s=p(s,t,i,r,e[o+10],9,38016083),r=p(r,s,t,i,e[o+15],14,-660478335),i=p(i,r,s,t,e[o+4],20,-405537848),t=p(t,i,r,s,e[o+9],5,568446438),s=p(s,t,i,r,e[o+14],9,-1019803690),r=p(r,s,t,i,e[o+3],14,-187363961),i=p(i,r,s,t,e[o+8],20,1163531501),t=p(t,i,r,s,e[o+13],5,-1444681467),s=p(s,t,i,r,e[o+2],9,-51403784),r=p(r,s,t,i,e[o+7],14,1735328473),i=p(i,r,s,t,e[o+12],20,-1926607734),t=f(t,i,r,s,e[o+5],4,-378558),s=f(s,t,i,r,e[o+8],11,-2022574463),r=f(r,s,t,i,e[o+11],16,1839030562),i=f(i,r,s,t,e[o+14],23,-35309556),t=f(t,i,r,s,e[o+1],4,-1530992060),s=f(s,t,i,r,e[o+4],11,1272893353),r=f(r,s,t,i,e[o+7],16,-155497632),i=f(i,r,s,t,e[o+10],23,-1094730640),t=f(t,i,r,s,e[o+13],4,681279174),s=f(s,t,i,r,e[o+0],11,-358537222),r=f(r,s,t,i,e[o+3],16,-722521979),i=f(i,r,s,t,e[o+6],23,76029189),t=f(t,i,r,s,e[o+9],4,-640364487),s=f(s,t,i,r,e[o+12],11,-421815835),r=f(r,s,t,i,e[o+15],16,530742520),i=f(i,r,s,t,e[o+2],23,-995338651),t=d(t,i,r,s,e[o+0],6,-198630844),s=d(s,t,i,r,e[o+7],10,1126891415),r=d(r,s,t,i,e[o+14],15,-1416354905),i=d(i,r,s,t,e[o+5],21,-57434055),t=d(t,i,r,s,e[o+12],6,1700485571),s=d(s,t,i,r,e[o+3],10,-1894986606),r=d(r,s,t,i,e[o+10],15,-1051523),i=d(i,r,s,t,e[o+1],21,-2054922799),t=d(t,i,r,s,e[o+8],6,1873313359),s=d(s,t,i,r,e[o+15],10,-30611744),r=d(r,s,t,i,e[o+6],15,-1560198380),i=d(i,r,s,t,e[o+13],21,1309151649),t=d(t,i,r,s,e[o+4],6,-145523070),s=d(s,t,i,r,e[o+11],10,-1120210379),r=d(r,s,t,i,e[o+2],15,718787259),i=d(i,r,s,t,e[o+9],21,-343485551),t=y(t,g),i=y(i,h),r=y(r,S),s=y(s,P)}return Array(t,i,r,s)}function b(e,n,t,i,r,s){return y(Ie(y(y(n,e),y(i,s)),r),t)}function c(e,n,t,i,r,s,o){return b(n&t|~n&i,e,n,r,s,o)}function p(e,n,t,i,r,s,o){return b(n&i|t&~i,e,n,r,s,o)}function f(e,n,t,i,r,s,o){return b(n^t^i,e,n,r,s,o)}function d(e,n,t,i,r,s,o){return b(t^(n|~i),e,n,r,s,o)}function y(e,n){var t=(e&65535)+(n&65535),i=(e>>16)+(n>>16)+(t>>16);return i<<16|t&65535}function Ie(e,n){return e<<n|e>>>32-n}var F={md5:function(e){return ve(e)}},R={request(e,n,t,i,r=!1){return new Promise((s,o)=>{let g=!1;g=getApp().globalData.token;let h={"content-type":"application/json",ua:getApp().globalData.ua};g&&(h.Authorization=`Bearer ${g}`);let S=getApp().globalData.apiHost;n.nonce=F.md5(se()+new Date().getTime());let P=Pe(n);uni.request({url:S+e,method:"POST",header:h,data:P,dataType:"json",timeout:1e4,complete(w){return A(this,null,function*(){if(t&&uni.hideLoading(),w.statusCode==200){let v=w.data;if(C(v.msg)||N(v.msg),v.ok===99){yield getApp().createUser();let Q=yield R.request(e,n,t,i,r);s(Q);return}if(v.ok>0){s(v);return}i&&o(v)}else if(i)o(w);else return Se})},fail(w){}})})}};function Pe(e){try{C(e)&&(e={}),e.times=new Date().getTime();let n=Object.keys(e).sort(),t={};for(let r=0,s=n.length;r<s;r++)t[n[r]]=e[n[r]];let i=J(t).substr(1);return t.sign=F.md5(`${F.md5(i)}${_.key}`),t}catch(n){return e}}function J(e,n){if(e==null)return"";try{let t="",i=typeof e;if(i=="string"||i=="number")t+="&"+n+"="+ne(e);else if(i=="boolean")t+="&"+n+"="+(e?1:0);else for(let r in e){let s=n==null?r:n+Ee(r);t+=J(e[r],s)}return t}catch(t){return""}}function Ee(e){return`${encodeURIComponent("[")}${e}${encodeURIComponent("]")}`}var Y={CALL:1,ANSWER:2,MATCH_ACCEPT:3,MATCH_CONNECT:4,CALL_HANG:5},Te={"l-img":{"":{flexDirection:"row",alignItems:"center",justifyContent:"center"}},"place-img":{"":{position:"absolute",top:0,left:0}},"text-place":{"":{color:"#ffffff",position:"absolute",bottom:"0rpx",textAlign:"center"}}},Me={name:"LImg",props:{src:{type:String,default:""},mode:{type:String,default:"aspectFill"},w:{type:String,default:"0"},h:{type:String,default:"0"},placeW:{type:String,default:"0"},placeFontSize:{type:[String,Boolean],default:""},radius:{type:String,default:"0"},lazyLoad:{type:Boolean,default:!1},fade:{type:Boolean,default:!0},border:{type:String,default:""},otherStyle:{type:Object,default(){return{}}},click:{type:Boolean,default:!0},useNativePic:{type:Boolean,default:!0}},data(){return{isLoad:!1,placeSrc:"/static/img_load_s.png",path:""}},watch:{src(){this.isLoad=!1,this.path=this.parse()}},computed:{viewStyle(){let e={width:this.w,height:this.h};return this.setRadius(e)},imgStyle(){let e={width:this.w,height:this.h};return e=Object.assign(e,this.otherStyle),this.setRadius(e)},borderStyle(){return this.border?{border:this.border}:{}},placeViewStyle(){return{width:this.placeW,height:this.placeW,position:"relative"}},placeFontSizeVal(){return this.placeFontSize===!1?"":this.placeFontSize?this.placeFontSize:"16rpx"}},methods:{clickImg(e){this.$emit("click",e)},setRadius(e){let n=this.radius.split(" ");return n.length<=1?e.borderRadius=this.radius:(e.borderTopLeftRadius=n[0],e.borderTopRightRadius=n[1],e.borderBottomRightRadius=n[2],e.borderBottomLeftRadius=n[3]),e},onLoad(){this.path&&(this.isLoad=!0,this.$emit("onLoad"))},parse(){return this.src?this.useNativePic?this.src:this.src.startsWith("file://")?this.src:this.src.startsWith("/var/mobile/")?"file://"+this.src:this.src.startsWith("/storage/")?this.src:this.src.startsWith("https")?this.src:this.useNativePic?this.src:_.ossPicPath+this.src:""}},created(){this.path=this.parse()}};function Fe(e,n,t,i,r,s){return(0,l.openBlock)(),(0,l.createElementBlock)("view",{class:"l-img",onClick:n[1]||(n[1]=(...o)=>s.clickImg&&s.clickImg(...o)),style:(0,l.normalizeStyle)([s.viewStyle,s.borderStyle]),renderWhole:!0},[(0,l.createElementVNode)("view",{class:"l-img",style:(0,l.normalizeStyle)([{position:"relative"},s.viewStyle])},[r.isLoad?(0,l.createCommentVNode)("",!0):((0,l.openBlock)(),(0,l.createElementBlock)("view",{key:0,style:(0,l.normalizeStyle)(s.placeViewStyle)},[(0,l.createElementVNode)("u-image",{src:r.placeSrc,style:(0,l.normalizeStyle)({width:t.placeW,height:t.placeW})},null,12,["src"]),s.placeFontSizeVal?((0,l.openBlock)(),(0,l.createElementBlock)("u-text",{key:0,class:"text-place",style:(0,l.normalizeStyle)({fontSize:s.placeFontSizeVal,width:t.placeW})},"Loading...",4)):(0,l.createCommentVNode)("",!0)],4)),(0,l.createElementVNode)("u-image",{onLoad:n[0]||(n[0]=(...o)=>s.onLoad&&s.onLoad(...o)),src:r.path,mode:t.mode,lazyLoad:t.lazyLoad,fadeShow:t.fade,style:(0,l.normalizeStyle)([s.imgStyle,s.borderStyle]),class:"place-img"},null,44,["src","mode","lazyLoad","fadeShow"])],4)],4)}var K=L(Me,[["render",Fe],["styles",[Te]]]);var a=H(E());function Le(e){let t={path:"",repeat:!0,isOut:!!e};getApp().globalData.audioModule.startRing(t,i=>{})}function Oe(e){getApp().globalData.audioModule.stopRing({isConnectCall:e?1:0})}var k=getApp().globalData.window,Ne={computed:{whStyle(){return{width:`${this.sW}px`,height:`${this.sH}px`}}},data(){return{sW:k.width,sH:k.height,statusBarH:k.statusBarHeight,user:{},isClose:!1,engine:void 0,ringTime:0,timer:void 0,actionType:1,permissionCallStatus:0,permissionCallHasSetting:!1}},methods:{clickHangup(e){W(()=>{this.isClose=!0,this.ringStop(),this.getHangReply(e),uni.navigateBack(),uni.$emit("event_hung_up_notice",e)},500,"video-hangup-click")},getHangReply(e){let n={msg_type:Y.CALL_HANG,msg_content:{end_type:e}};R.request("/call/end",n)},ringPlay(){this.isClose||Le(!0)},ringStop(){Oe(!1)},checkCallPermission(){return A(this,null,function*(){return(yield O(u.MICROPHONE,!0,!1))?(yield O(u.CAMERA,!0,!1))?!0:(this.permissionCallStatus=2,D(u.CAMERA,t=>{t&&(this.permissionCallHasSetting=!0)}),!1):(this.permissionCallStatus=2,D(u.MICROPHONE,t=>{t&&(this.permissionCallHasSetting=!0)}),!1)})},initView(){setTimeout(()=>{this.ringPlay(),this.ringTime=new Date().getTime(),setInterval(()=>{let e=new Date().getTime();e-this.ringTime>5*1e3&&N("Customer service is busy"),e-this.ringTime>10*1e3&&this.clickHangup(2)},2e3)},1e3)}},onShow(){return A(this,null,function*(){if(this.permissionCallHasSetting&&this.permissionCallStatus==2){if(this.permissionCallHasSetting=!1,!(yield this.checkCallPermission()))return;this.permissionCallStatus=1,this.initView()}})},onLoad(){return A(this,null,function*(){this.user={avatar:getApp().globalData.customerAvatar},(yield this.checkCallPermission())&&this.initView()})},onBackPress(e){e.from=="backbutton"&&(this.isClose=!0,this.ringStop())}},Re={pos:{"":{position:"absolute",top:0,left:0}},center:{"":{flexDirection:"row",alignItems:"center"}},"center-align":{"":{flexDirection:"row",alignItems:"center",justifyContent:"center"}},"center-content":{"":{alignItems:"center",justifyContent:"center"}},"view-loading":{"":{flex:1}},"view-video-content":{".view-loading ":{flex:1,alignItems:"center",justifyContent:"center"}},"video-view":{".view-loading .view-video-content ":{backgroundColor:"rgba(0,0,0,0)"}},"view-avatar-content":{".view-loading ":{flex:1,alignItems:"center",justifyContent:"center"}},bg:{".view-loading .view-avatar-content ":{backgroundColor:"#000000",opacity:.5}},"text-tip":{"":{backgroundColor:"rgba(0,0,0,0.2)",borderRadius:"8rpx",paddingTop:"12rpx",paddingRight:"12rpx",paddingBottom:"12rpx",paddingLeft:"12rpx",textAlign:"center",fontSize:"24rpx",color:"#FFFFFF"}},"view-chat":{"":{flex:1,backgroundColor:"rgba(0,0,0,0)"}},"text-connecting":{"":{fontSize:"28rpx",fontWeight:"600",color:"#FFFFFF",textAlign:"center"}},text:{"":{fontSize:"24rpx",marginTop:"8rpx",color:"#FFFFFF"}}},ke={mixins:[Ne],components:{LImg:K}};function Ve(e,n,t,i,r,s){let o=(0,a.resolveComponent)("l-img");return(0,a.openBlock)(),(0,a.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,a.createElementVNode)("view",{class:"chat-view",style:(0,a.normalizeStyle)([e.whStyle,{background:"#000000"}])},[(0,a.createElementVNode)("view",{class:"view-loading"},[(0,a.createElementVNode)("view",{class:"view-video-content"},[(0,a.createElementVNode)("view",{class:"view-avatar-content"},[e.user.avatar?((0,a.openBlock)(),(0,a.createBlock)(o,{key:0,mode:"aspectFill",src:e.user.avatar,w:`${e.sW}px`,h:`${e.sH}px`},null,8,["src","w","h"])):(0,a.createCommentVNode)("",!0),(0,a.createElementVNode)("view",{class:"bg pos",style:(0,a.normalizeStyle)(e.whStyle)},null,4)])]),(0,a.createElementVNode)("view",{style:(0,a.normalizeStyle)([{position:"absolute","justify-content":"center","align-items":"center"},{top:`${e.statusBarH+120}px`,width:`${e.sW}px`}])},[(0,a.createElementVNode)("view",null,[e.user.avatar?((0,a.openBlock)(),(0,a.createBlock)(o,{key:0,src:e.user.avatar,w:"264rpx",h:"264rpx",radius:"132rpx",border:"1px solid #FFFFFF"},null,8,["src"])):(0,a.createCommentVNode)("",!0)]),(0,a.createElementVNode)("view",{style:{"margin-top":"40rpx",width:"520rpx"},class:"text-tip"},[(0,a.createElementVNode)("u-text",{style:{"font-size":"28rpx",color:"#FFFFFF"}},"\u26A0\uFE0F This call may be recorded\uFF0CPlease chat in a civilized way.")])],4)]),(0,a.createElementVNode)("view",{class:"center-align",style:(0,a.normalizeStyle)([{position:"absolute",bottom:"100rpx",left:"0rpx"},{width:`${e.sW}px`}])},[(0,a.createElementVNode)("view",null,[(0,a.createElementVNode)("view",{class:"center-align"},[(0,a.createVNode)(o,{onClick:n[0]||(n[0]=g=>e.clickHangup(1)),src:"/static/hang_up_1.png",w:"144rpx",h:"144rpx"})])])],4)],4)])}var I=L(ke,[["render",Ve],["styles",[Re]]]);var V=plus.webview.currentWebview();if(V){let e=parseInt(V.id),n="pages/chat/call",t={};try{t=JSON.parse(V.__query__)}catch(r){}I.mpType="page";let i=Vue.createPageApp(I,{$store:getApp({allowDefault:!0}).$store,__pageId:e,__pagePath:n,__pageQuery:t});i.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...I.styles||[]])),i.mount("#root")}})();