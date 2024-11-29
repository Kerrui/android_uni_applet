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


(()=>{var h=Object.create;var m=Object.defineProperty;var w=Object.getOwnPropertyDescriptor;var y=Object.getOwnPropertyNames;var T=Object.getPrototypeOf,k=Object.prototype.hasOwnProperty;var E=(e,o)=>()=>(o||e((o={exports:{}}).exports,o),o.exports);var C=(e,o,t,i)=>{if(o&&typeof o=="object"||typeof o=="function")for(let s of y(o))!k.call(e,s)&&s!==t&&m(e,s,{get:()=>o[s],enumerable:!(i=w(o,s))||i.enumerable});return e};var g=(e,o,t)=>(t=e!=null?h(T(e)):{},C(o||!e||!e.__esModule?m(t,"default",{value:e,enumerable:!0}):t,e));var u=E((D,d)=>{d.exports=Vue});var I=g(u());var _=(e,o)=>{let t=e.__vccOpts||e;for(let[i,s]of o)t[i]=s;return t};var B={zT:2,appId:plus.runtime.appid,appV:parseInt(plus.runtime.version.split(".").join("")),isAndroid:plus.os.name=="Android"};function l(e,o,t,i="Confirm"){x(e,o,t,"",i,!1)}function x(e,o,t,i,s="Confirm",a=!0,r="Cancel",v=!1,b){uni.showModal({title:e,content:o,showCancel:a,cancelText:r,confirmText:s,editable:v,placeholderText:b,success(f){f.confirm?typeof t=="function"&&t(f):f.cancel&&typeof i=="function"&&i()}})}var n=g(u());function A(e){var o;typeof getApp().globalData.pluginEvent.getToken=="function"&&((o=getApp().globalData.pluginEvent)==null||o.getToken({},t=>{typeof e=="function"&&e(t)}))}function P(e,o,t){var i;typeof getApp().globalData.pluginEvent.fireEvent=="function"&&((i=getApp().globalData.pluginEvent)==null||i.fireEvent({event:e,params:o},s=>{typeof t=="function"&&t(s)}))}function F(e,o,t,i){var s;typeof getApp().globalData.pluginEvent.fbEvent=="function"&&((s=getApp().globalData.pluginEvent)==null||s.fbEvent({event:e,params:o,valueToSum:t},a=>{typeof i=="function"&&i(a)}))}var N={methods:{clickGetToken(){A(e=>{l("firebase token","token:"+e)})},clickFireEvent(){P("UNI_TEST",{uni:"UNI_TEST"},e=>{l("firebase event","event:"+e)})},clickFbEvent(){F("UNI_TEST",{uni:"UNI_TEST"},12,e=>{l("facebook event","event:"+e)})}}};function S(e,o,t,i,s,a){let r=(0,n.resolveComponent)("button");return(0,n.openBlock)(),(0,n.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,n.createElementVNode)("div",null,[(0,n.createVNode)(r,{onClick:a.clickGetToken},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u83B7\u53D6Tokens")]),_:1},8,["onClick"]),(0,n.createVNode)(r,{onClick:a.clickFireEvent},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("firebase\u8BB0\u5F55\u4E8B\u4EF6")]),_:1},8,["onClick"]),(0,n.createVNode)(r,{onClick:a.clickFbEvent},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("facebook\u8BB0\u5F55\u4E8B\u4EF6")]),_:1},8,["onClick"])])])}var c=_(N,[["render",S]]);var p=plus.webview.currentWebview();if(p){let e=parseInt(p.id),o="pagesPub/event-view",t={};try{t=JSON.parse(p.__query__)}catch(s){}c.mpType="page";let i=Vue.createPageApp(c,{$store:getApp({allowDefault:!0}).$store,__pageId:e,__pagePath:o,__pageQuery:t});i.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...c.styles||[]])),i.mount("#root")}})();
