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


(()=>{var C=Object.create;var m=Object.defineProperty;var P=Object.getOwnPropertyDescriptor;var S=Object.getOwnPropertyNames;var _=Object.getPrototypeOf,v=Object.prototype.hasOwnProperty;var F=(t,e)=>()=>(e||t((e={exports:{}}).exports,e),e.exports);var V=(t,e,n,l)=>{if(e&&typeof e=="object"||typeof e=="function")for(let i of S(e))!v.call(t,i)&&i!==n&&m(t,i,{get:()=>e[i],enumerable:!(l=P(e,i))||l.enumerable});return t};var d=(t,e,n)=>(n=t!=null?C(_(t)):{},V(e||!t||!t.__esModule?m(n,"default",{value:t,enumerable:!0}):n,t));var f=F((j,A)=>{A.exports=Vue});var O=new Promise(()=>{});function h(t,e){typeof getApp().globalData.pluginTool.writeLog=="function"&&getApp().globalData.pluginTool.writeLog({type:t,logMessage:e})}function g(){if(typeof getApp().globalData.pluginTool.logMessagePath=="function")return getApp().globalData.pluginTool.logMessagePath()}function b(){typeof getApp().globalData.pluginTool.deleteAllCrashFiles=="function"&&getApp().globalData.pluginTool.deleteAllCrashFiles()}function y(t){typeof getApp().globalData.pluginTool.deleteCrashFilePath=="function"&&getApp().globalData.pluginTool.deleteCrashFilePath({filePath:t})}var R=d(f());var D=(t,e)=>{let n=t.__vccOpts||t;for(let[l,i]of e)n[l]=i;return n};var E={zT:2,appId:plus.runtime.appid,appV:parseInt(plus.runtime.version.split(".").join("")),isAndroid:plus.os.name=="Android"};function w(t,e,n,l,i="Confirm",a=!0,p="Cancel",r=!1,T){uni.showModal({title:t,content:e,showCancel:a,cancelText:p,confirmText:i,editable:r,placeholderText:T,success(u){u.confirm?typeof n=="function"&&n(u):u.cancel&&typeof l=="function"&&l()}})}var o=d(f());var x={"input-view":{"":{marginTop:"20rpx",width:200,height:44,lineHeight:44,color:"#333333",borderWidth:1,borderStyle:"solid",borderColor:"#FF4477"}}},k={data(){return{level:"0",text:""}},methods:{clickWriteLog(){let t=Number(this.level),e=this.text;h(t,e)},clickLogMessagePath(){w("\u65E5\u5FD7\u8DEF\u5F84",g())},clickDeleteAllCrashFiles(){b()},clickDeleteCrashFilePath(){y(g())}}};function L(t,e,n,l,i,a){let p=(0,o.resolveComponent)("button");return(0,o.openBlock)(),(0,o.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,o.createElementVNode)("div",{style:{flex:"1"}},[(0,o.createElementVNode)("u-input",{class:"input-view",inputmode:"numeric",modelValue:i.level,onInput:e[0]||(e[0]=r=>i.level=r.detail.value),placeholder:"\u65E5\u5FD7\u7B49\u7EA7"},null,40,["modelValue"]),(0,o.createElementVNode)("u-input",{class:"input-view",inputmode:"text",modelValue:i.text,onInput:e[1]||(e[1]=r=>i.text=r.detail.value),placeholder:"\u65E5\u5FD7"},null,40,["modelValue"]),(0,o.createVNode)(p,{onClick:a.clickWriteLog},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u5199\u65E5\u5FD7")]),_:1},8,["onClick"]),(0,o.createVNode)(p,{onClick:a.clickLogMessagePath},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u65E5\u5FD7\u8DEF\u5F84")]),_:1},8,["onClick"]),(0,o.createVNode)(p,{onClick:a.clickDeleteAllCrashFiles},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u5220\u9664\u6240\u6709crash\u65E5\u5FD7")]),_:1},8,["onClick"]),(0,o.createVNode)(p,{onClick:a.clickDeleteCrashFilePath},{default:(0,o.withCtx)(()=>[(0,o.createTextVNode)("\u5220\u9664\u8DEF\u5F84\u6587\u4EF6")]),_:1},8,["onClick"])])])}var s=D(k,[["render",L],["styles",[x]]]);var c=plus.webview.currentWebview();if(c){let t=parseInt(c.id),e="pagesPub/log-view",n={};try{n=JSON.parse(c.__query__)}catch(i){}s.mpType="page";let l=Vue.createPageApp(s,{$store:getApp({allowDefault:!0}).$store,__pageId:t,__pagePath:e,__pageQuery:n});l.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...s.styles||[]])),l.mount("#root")}})();
