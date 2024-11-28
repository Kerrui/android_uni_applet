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


(()=>{var D=Object.create;var f=Object.defineProperty;var S=Object.getOwnPropertyDescriptor;var T=Object.getOwnPropertyNames;var _=Object.getPrototypeOf,w=Object.prototype.hasOwnProperty;var C=(t,e)=>()=>(e||t((e={exports:{}}).exports,e),e.exports);var P=(t,e,n,i)=>{if(e&&typeof e=="object"||typeof e=="function")for(let a of T(e))!w.call(t,a)&&a!==n&&f(t,a,{get:()=>e[a],enumerable:!(i=S(e,a))||i.enumerable});return t};var c=(t,e,n)=>(n=t!=null?D(_(t)):{},P(e||!t||!t.__esModule?f(n,"default",{value:t,enumerable:!0}):n,t));var u=C((I,m)=>{m.exports=Vue});var L=c(u());var d=(t,e)=>{let n=t.__vccOpts||t;for(let[i,a]of e)n[i]=a;return n};var W={zT:2,appId:plus.runtime.appid,appV:parseInt(plus.runtime.version.split(".").join("")),isAndroid:plus.os.name=="Android"};function h(t,e,n,i,a="Confirm",l=!0,p="Cancel",b=!1,y){uni.showModal({title:t,content:e,showCancel:l,cancelText:p,confirmText:a,editable:b,placeholderText:y,success(s){s.confirm?typeof n=="function"&&n(s):s.cancel&&typeof i=="function"&&i()}})}var M=new Promise(()=>{});function A(t){typeof getApp().globalData.pluginTool.showFileDocumentView=="function"&&getApp().globalData.pluginTool.showFileDocumentView({},e=>{typeof t=="function"&&t(e.file_list)})}var o=c(u());function v(){var t;typeof getApp().globalData.pluginAudio.startRing=="function"&&((t=getApp().globalData.pluginAudio)==null||t.startRing())}function F(t){var e;typeof getApp().globalData.pluginAudio.stopRing=="function"&&((e=getApp().globalData.pluginAudio)==null||e.stopRing({isConnectCall:t}))}function V(t){var e;if(typeof getApp().globalData.pluginAudio.playSound=="function")return(e=getApp().globalData.pluginAudio)==null?void 0:e.playSound({path:t})}var k={methods:{clickStartRing(){v()},clickStopRing(){F(!1)},clickOpenFileSelector(){A(t=>{let e=t[0];h("Success",JSON.stringify(e),()=>{V(e.path)},"","\u64AD\u653E")})}}};function x(t,e,n,i,a,l){let p=(0,o.resolveComponent)("button");return(0,o.openBlock)(),(0,o.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,o.createElementVNode)("div",null,[(0,o.createVNode)(p,{onClick:l.clickStartRing},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u54CD\u94C3")]),_:1},8,["onClick"]),(0,o.createVNode)(p,{onClick:l.clickStopRing},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u6302\u65AD")]),_:1},8,["onClick"]),(0,o.createVNode)(p,{onClick:l.clickOpenFileSelector},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u9009\u62E9\u97F3\u9891\u64AD\u653E")]),_:1},8,["onClick"])])])}var r=d(k,[["render",x]]);var g=plus.webview.currentWebview();if(g){let t=parseInt(g.id),e="pagesPub/audio-view",n={};try{n=JSON.parse(g.__query__)}catch(a){}r.mpType="page";let i=Vue.createPageApp(r,{$store:getApp({allowDefault:!0}).$store,__pageId:t,__pagePath:e,__pageQuery:n});i.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...r.styles||[]])),i.mount("#root")}})();
