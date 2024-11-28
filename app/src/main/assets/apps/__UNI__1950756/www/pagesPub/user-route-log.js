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


(()=>{var b=Object.create;var u=Object.defineProperty;var y=Object.getOwnPropertyDescriptor;var k=Object.getOwnPropertyNames;var x=Object.getPrototypeOf,C=Object.prototype.hasOwnProperty;var S=(e,t)=>()=>(t||e((t={exports:{}}).exports,t),t.exports);var v=(e,t,o,s)=>{if(t&&typeof t=="object"||typeof t=="function")for(let n of k(t))!C.call(e,n)&&n!==o&&u(e,n,{get:()=>t[n],enumerable:!(s=y(t,n))||s.enumerable});return e};var f=(e,t,o)=>(o=e!=null?b(x(e)):{},v(t||!e||!e.__esModule?u(o,"default",{value:e,enumerable:!0}):o,e));var p=S((N,_)=>{_.exports=Vue});var W=f(p());function g(e,t,...o){uni.__log__?uni.__log__(e,t,...o):console[e].apply(console,[...o,t])}var m=(e,t)=>{let o=e.__vccOpts||e;for(let[s,n]of t)o[s]=n;return o};function w(e){return typeof e=="undefined"||e==null||e==""}var j=new Promise(()=>{});function d(e,t,o=""){if(w(e))return"";let s=[];for(let n in e)s.push(n+"="+e[n]);return o+s.join(t)}function h(e,t=null,o=null,s=!1){let n=O(e,t),a={url:n,fail:i=>{g("error","at common/route.js:12",`\u6253\u5F00\u9875\u9762\u5931\u8D25${n} ${JSON.stringify(i)}`)}};s&&(a=Object.assign(a,s)),o!=null&&(a.events=o),uni.navigateTo(a)}function O(e,t){return e+d(t,"&","?")}var r=f(p());var P={methods:{click1(){h("/pagesPub/user-log")},click2(){uni.navigateBack()}}};function T(e,t,o,s,n,a){let i=(0,r.resolveComponent)("button");return(0,r.openBlock)(),(0,r.createElementBlock)("scroll-view",{scrollY:!0,showScrollbar:!0,enableBackToTop:!0,bubble:"true",style:{flexDirection:"column"}},[(0,r.createVNode)(i,{onClick:a.click1},{default:(0,r.withCtx)(()=>[(0,r.createTextVNode)("\u8DF3\u8F6C\u7528\u6237\u65E5\u5FD7\u9875\u9762")]),_:1},8,["onClick"]),(0,r.createVNode)(i,{onClick:a.click2},{default:(0,r.withCtx)(()=>[(0,r.createTextVNode)("\u8FD4\u56DE")]),_:1},8,["onClick"])])}var c=m(P,[["render",T]]);var l=plus.webview.currentWebview();if(l){let e=parseInt(l.id),t="pagesPub/user-route-log",o={};try{o=JSON.parse(l.__query__)}catch(n){}c.mpType="page";let s=Vue.createPageApp(c,{$store:getApp({allowDefault:!0}).$store,__pageId:e,__pagePath:t,__pageQuery:o});s.provide("__globalStyles",Vue.useCssStyles([...__uniConfig.styles,...c.styles||[]])),s.mount("#root")}})();
