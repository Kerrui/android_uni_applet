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


(()=>{var v=Object.create;var m=Object.defineProperty;var y=Object.getOwnPropertyDescriptor;var I=Object.getOwnPropertyNames;var h=Object.getPrototypeOf,C=Object.prototype.hasOwnProperty;var w=(t,e)=>()=>(e||t((e={exports:{}}).exports,e),e.exports);var j=(t,e,o,i)=>{if(e&&typeof e=="object"||typeof e=="function")for(let l of I(e))!C.call(t,l)&&l!==o&&m(t,l,{get:()=>e[l],enumerable:!(i=y(e,l))||i.enumerable});return t};var A=(t,e,o)=>(o=t!=null?v(h(t)):{},j(e||!t||!t.__esModule?m(o,"default",{value:t,enumerable:!0}):o,t));var p=w((F,_)=>{_.exports=Vue});var L=A(p());function f(t,e,...o){uni.__log__?uni.__log__(t,e,...o):console[t].apply(console,[...o,e])}var b=(t,e)=>{let o=t.__vccOpts||t;for(let[i,l]of e)o[i]=l;return o};var M={zT:2,appId:plus.runtime.appid,appV:parseInt(plus.runtime.version.split(".").join("")),isAndroid:plus.os.name=="Android"};function s(t,e,o,i="Confirm"){D(t,e,o,"",i,!1)}function D(t,e,o,i,l="Confirm",a=!0,u="Cancel",c=!1,k){uni.showModal({title:t,content:e,showCancel:a,cancelText:u,confirmText:l,editable:c,placeholderText:k,success(d){d.confirm?typeof o=="function"&&o(d):d.cancel&&typeof i=="function"&&i()}})}var n=A(p());function V(t,e,o,i){var l;typeof getApp().globalData.pluginAdjust.init=="function"&&((l=getApp().globalData.pluginAdjust)==null||l.init({appToken:t,sessionBack:e,eventBack:o},a=>{typeof i=="function"&&i(a)}))}function S(t,e){var o;typeof getApp().globalData.pluginAdjust.event=="function"&&((o=getApp().globalData.pluginAdjust)==null||o.event({token:t},i=>{typeof e=="function"&&e(i)}))}function x(){var t;if(typeof getApp().globalData.pluginAdjust.getAttribution=="function")return(t=getApp().globalData.pluginAdjust)==null?void 0:t.getAttribution({},null)}function P(){var t;if(typeof getApp().globalData.pluginAdjust.getAdId=="function")return(t=getApp().globalData.pluginAdjust)==null?void 0:t.getAdId({},null)}function G(){var t;if(typeof getApp().globalData.pluginAdjust.getAmazonId=="function")return(t=getApp().globalData.pluginAdjust)==null?void 0:t.getAmazonId({},null)}function N(){var t;typeof getApp().globalData.pluginAdjust.getGoogleAdId=="function"&&((t=getApp().globalData.pluginAdjust)==null||t.getGoogleAdId({},e=>{typeof callback=="function"&&callback(dafta)}))}function O(t){var e;typeof getApp().globalData.pluginAdjust.setEnabled=="function"&&((e=getApp().globalData.pluginAdjust)==null||e.setEnabled({enable:t}))}function J(){var t;if(typeof getApp().globalData.pluginAdjust.sdkVersion!="function")return;let e=(t=getApp({}).globalData.pluginAdjust)==null?void 0:t.sdkVersion();return f("log","at comPlugin/adjust.js:53",`verison==>${e}`),e}var E={data(){return{enable:!1,token:""}},methods:{clickInit(){V(this.token,!0,!0,t=>{s("init",JSON.stringify(t))})},clickEvent(){S(this.token,t=>{s("event",JSON.stringify(t))})},clickGetAttribution(){let t=x();s("attribution",JSON.stringify(t))},clickGetAdId(){let t=P();s("AdId",JSON.stringify(t))},clickGetAmazonId(){let t=G();s("AmazonId",JSON.stringify(t))},clickGetGoogleAdId(){N()},clickSetEnable(){this.enable=!this.enable,O(this.enable)},clickSdkVersion(){let t=J();s("sdkVersion",t)}}};function T(t,e,o,i,l,a){let u=(0,n.resolveComponent)("button");return(0,n.openBlock)(),(0,n.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,n.createElementVNode)("div",null,[(0,n.createElementVNode)("u-input",{modelValue:l.token,onInput:e[0]||(e[0]=c=>l.token=c.detail.value),placeholder:"token",style:{border:"solid #000 1px"}},null,40,["modelValue"]),(0,n.createVNode)(u,{onClick:a.clickInit},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u521D\u59CB\u5316")]),_:1},8,["onClick"]),(0,n.createVNode)(u,{onClick:a.clickEvent},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("event")]),_:1},8,["onClick"]),(0,n.createVNode)(u,{onClick:a.clickGetAttribution},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u83B7\u53D6\u5C5E\u6027")]),_:1},8,["onClick"]),(0,n.createVNode)(u,{onClick:a.clickGetAdId},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u83B7\u53D6AdId")]),_:1},8,["onClick"]),(0,n.createVNode)(u,{onClick:a.clickGetAmazonId},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u83B7\u53D6AmazonId")]),_:1},8,["onClick"]),(0,n.createVNode)(u,{onClick:a.clickGetGoogleAdId},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u83B7\u53D6GoogleAdId")]),_:1},8,["onClick"]),(0,n.createVNode)(u,{onClick:a.clickSetEnable},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u8BBE\u7F6EEnable("+(0,n.toDisplayString)(l.enable)+")",1)]),_:1},8,["onClick"]),(0,n.createVNode)(u,{onClick:a.clickSdkVersion},{default:(0,n.withCtx)(()=>[(0,n.createTextVNode)("\u7248\u672C\u53F7")]),_:1},8,["onClick"])])])}var r=b(E,[["render",T]]);var g=plus.webview.currentWebview();if(g){let t=parseInt(g.id),e="pagesPub/adjust-view",o={};try{o=JSON.parse(g.__query__)}catch(l){}r.mpType="page";let i=Vue.createPageApp(r,{$store:getApp({allowDefault:!0}).$store,__pageId:t,__pagePath:e,__pageQuery:o});i.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...r.styles||[]])),i.mount("#root")}})();
