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


(()=>{var h=Object.create;var g=Object.defineProperty;var w=Object.getOwnPropertyDescriptor;var P=Object.getOwnPropertyNames;var I=Object.getPrototypeOf,k=Object.prototype.hasOwnProperty;var O=(e,t)=>()=>(t||e((t={exports:{}}).exports,t),t.exports);var C=(e,t,o,i)=>{if(t&&typeof t=="object"||typeof t=="function")for(let a of P(t))!k.call(e,a)&&a!==o&&g(e,a,{get:()=>t[a],enumerable:!(i=w(t,a))||i.enumerable});return e};var m=(e,t,o)=>(o=e!=null?h(I(e)):{},C(t||!e||!e.__esModule?g(o,"default",{value:e,enumerable:!0}):o,e));var f=O((J,d)=>{d.exports=Vue});var T=m(f());var _=(e,t)=>{let o=e.__vccOpts||e;for(let[i,a]of t)o[i]=a;return o};var L={zT:2,appId:plus.runtime.appid,appV:parseInt(plus.runtime.version.split(".").join("")),isAndroid:plus.os.name=="Android"};function r(e,t,o,i="Confirm"){b(e,t,o,"",i,!1)}function b(e,t,o,i,a="Confirm",l=!0,s="Cancel",y=!1,v){uni.showModal({title:e,content:t,showCancel:l,cancelText:s,confirmText:a,editable:y,placeholderText:v,success(c){c.confirm?typeof o=="function"&&o(c):c.cancel&&typeof i=="function"&&i()}})}var n=m(f());function A(e,t){var o;typeof getApp().globalData.pluginOpenInstall.getInstallParams=="function"&&((o=getApp().globalData.pluginOpenInstall)==null||o.getInstallParams({time:e},i=>{typeof t=="function"&&t(i)}))}function R(e){var t;typeof getApp().globalData.pluginOpenInstall.registerWakeUp=="function"&&((t=getApp().globalData.pluginOpenInstall)==null||t.registerWakeUp({},o=>{typeof e=="function"&&e(o)}))}function x(e){var t;typeof getApp().globalData.pluginOpenInstall.reportRegister=="function"&&((t=getApp().globalData.pluginOpenInstall)==null||t.reportRegister({},o=>{typeof e=="function"&&e(o)}))}function D(e,t,o){var i;typeof getApp().globalData.pluginOpenInstall.reportEffectPoint=="function"&&((i=getApp().globalData.pluginOpenInstall)==null||i.reportEffectPoint({pointId:e,value:t},a=>{typeof o=="function"&&o(a)}))}var N={methods:{clickgetInstallParams(){A(10,e=>{r("InstallParams",JSON.stringify(e))})},clickRegisterWakeUp(){R(e=>{r("RegisterWakeUp",JSON.stringify(e))})},clickReportRegister(){x(e=>{r("ReportRegister",JSON.stringify(e))})},clickReportEffectPoint(){D("12",12345,o=>{r("ReportEffectPoint",JSON.stringify(o))})}}};function S(e,t,o,i,a,l){let s=(0,n.resolveComponent)("button");return(0,n.openBlock)(),(0,n.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,n.createElementVNode)("div",null,[(0,n.createVNode)(s,{onClick:l.clickgetInstallParams},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u83B7\u53D6\u5E94\u7528\u5B89\u88C5\u4FE1\u606F")]),_:1},8,["onClick"]),(0,n.createVNode)(s,{onClick:l.clickRegisterWakeUp},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u6CE8\u518C\u5E94\u7528\u5524\u9192\u4FE1\u606F\u76D1\u542C")]),_:1},8,["onClick"]),(0,n.createVNode)(s,{onClick:l.clickReportRegister},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u5F00\u542F\u62A5\u544A")]),_:1},8,["onClick"]),(0,n.createVNode)(s,{onClick:l.clickReportEffectPoint},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u62A5\u544A\u4F5C\u7528\u70B9")]),_:1},8,["onClick"])])])}var p=_(N,[["render",S]]);var u=plus.webview.currentWebview();if(u){let e=parseInt(u.id),t="pagesPub/open-install-view",o={};try{o=JSON.parse(u.__query__)}catch(a){}p.mpType="page";let i=Vue.createPageApp(p,{$store:getApp({allowDefault:!0}).$store,__pageId:e,__pagePath:t,__pageQuery:o});i.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...p.styles||[]])),i.mount("#root")}})();
