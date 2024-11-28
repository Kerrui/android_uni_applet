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


(()=>{var I=Object.create;var _=Object.defineProperty;var w=Object.getOwnPropertyDescriptor;var D=Object.getOwnPropertyNames;var C=Object.getPrototypeOf,k=Object.prototype.hasOwnProperty;var y=(e,t)=>()=>(t||e((t={exports:{}}).exports,t),t.exports);var b=(e,t,n,i)=>{if(t&&typeof t=="object"||typeof t=="function")for(let l of D(t))!k.call(e,l)&&l!==n&&_(e,l,{get:()=>t[l],enumerable:!(i=w(t,l))||i.enumerable});return e};var g=(e,t,n)=>(n=e!=null?I(C(e)):{},b(t||!e||!e.__esModule?_(n,"default",{value:e,enumerable:!0}):n,e));var f=y((J,m)=>{m.exports=Vue});var U=g(f());var A=(e,t)=>{let n=e.__vccOpts||e;for(let[i,l]of t)n[i]=l;return n};var c={zT:2,appId:plus.runtime.appid,appV:parseInt(plus.runtime.version.split(".").join("")),isAndroid:plus.os.name=="Android"};function p(e,t,n,i="Confirm"){N(e,t,n,"",i,!1)}function N(e,t,n,i,l="Confirm",s=!0,a="Cancel",h=!1,v){uni.showModal({title:e,content:t,showCancel:s,cancelText:a,confirmText:l,editable:h,placeholderText:v,success(r){r.confirm?typeof n=="function"&&n(r):r.cancel&&typeof i=="function"&&i()}})}var o=g(f());function x(){return getApp().globalData.pluginApplet.bundleId()}function P(){c.isAndroid?plus.runtime.quit():getApp().globalData.pluginApplet.close()}function B(){return typeof getApp().globalData.pluginApplet.getDeviceModel!="function"?"":getApp().globalData.pluginApplet.getDeviceModel()}function V(e,t,n,i){let l={path:e,appid:t};n&&(l.info=n),c.isAndroid?uni.sendNativeEvent("open",l,s=>{i(s)}):getApp().globalData.pluginApplet.open(l,s=>{i(data)})}function O(e,t){typeof getApp().globalData.pluginApplet.setDefaultApplet=="function"&&getApp().globalData.pluginApplet.setDefaultApplet({appid:e,info:t})}function S(e,t){if(c.isAndroid)uni.sendNativeEvent("isInstall",e,n=>{t(n)});else return getApp().globalData.pluginApplet.isInstall({appid:e},n=>{typeof t=="function"&&t(n)})}var T={methods:{clickGetBundleId(){let e=x();p("BundleId",e)},clickClose(){P()},clickDeviceModel(){let e=B();p("DeviceModel",e)},clickAppletIsInstall(){S("__UNI__1950756",e=>{p("IsInstall","\u662F\u5426\u5B89\u88C5\uFF1A"+e)})},clickOpen(){V("/storage/emulated/0/Android/data/com.example.uni_applet/files/__UNI__5799B28.wgt","__UNI__5799B28",{info:"text"},n=>{p("open",JSON.stringify(n))})},clickSetDefaultApplet(){O("__UNI__1950756",{})}}};function F(e,t,n,i,l,s){let a=(0,o.resolveComponent)("button");return(0,o.openBlock)(),(0,o.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,o.createElementVNode)("div",null,[(0,o.createVNode)(a,{onClick:s.clickGetBundleId},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("Bundle ID")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:s.clickClose},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u5173\u95ED\u5C0F\u7A0B\u5E8F")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:s.clickDeviceModel},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("DeviceModel")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:s.clickAppletIsInstall},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u6536\u5426\u5B89\u88C5(__UNI__1950756)")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:s.clickOpen},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u6253\u5F00(__UNI__5799B28)")]),_:1},8,["onClick"]),(0,o.createVNode)(a,{onClick:s.clickSetDefaultApplet},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u8BBE\u7F6E\u9ED8\u8BA4\u5C0F\u7A0B\u5E8F(__UNI__1950756)")]),_:1},8,["onClick"])])])}var u=A(T,[["render",F]]);var d=plus.webview.currentWebview();if(d){let e=parseInt(d.id),t="pagesPub/applet-view",n={};try{n=JSON.parse(d.__query__)}catch(l){}u.mpType="page";let i=Vue.createPageApp(u,{$store:getApp({allowDefault:!0}).$store,__pageId:e,__pagePath:t,__pageQuery:n});i.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...u.styles||[]])),i.mount("#root")}})();
