import {DomSanitizer, SafeUrl} from "@angular/platform-browser";

export function toBase64(text: string): string {
  return Buffer.from(text).toString('base64');
}

export function fromBase64(text: string): string {
  return Buffer.from(text, 'base64').toString();
}

// export function toBlob(text: string): Promise<Blob> {
//   // const url = `https://www.openstreetmap.org/#map=17`
//   const url = `https://overpass-turbo.eu/map.html?Q=%2F*%0AThis%20has%20been%20generated%20by%20the%20overpass-turbo%20wizard.%0AThe%20original%20search%20was%3A%0A%E2%80%9Cleisure%3Dpitch%20and%20sport%3Dsoccer%20in%20Hungary%E2%80%9D%0A*%2F%0A%5Bout%3Ajson%5D%5Btimeout%3A25%5D%3B%0A%2F%2F%20fetch%20area%20%E2%80%9CBudapest%E2%80%9D%20to%20search%20in%0Aarea(id%3A3600037244)-%3E.searchArea%3B%0A%2F%2F%20gather%20results%0A(%0A%20%20%2F%2F%20query%20part%20for%3A%20%E2%80%9Cleisure%3Dpitch%20and%20sport%3Dsoccer%E2%80%9D%0A%20%20node%5B%22leisure%22%3D%22pitch%22%5D%5B%22sport%22%3D%22soccer%22%5D(area.searchArea)%3B%0A%20%20way%5B%22leisure%22%3D%22pitch%22%5D%5B%22sport%22%3D%22soccer%22%5D(area.searchArea)%3B%0A%20%20relation%5B%22leisure%22%3D%22pitch%22%5D%5B%22sport%22%3D%22soccer%22%5D(area.searchArea)%3B%0A)%3B%0A%2F%2F%20print%20results%0Aout%20body%3B%0A%3E%3B%0Aout%20skel%20qt%3B`
//   return fetch(url).then(res =>  res.blob());
// }
//
// export function toSafeUrl(text: string, sanitizer: DomSanitizer): Promise<SafeUrl> {
//   const url = `https://overpass-turbo.eu/map.html?Q=%2F*%0AThis%20has%20been%20generated%20by%20the%20overpass-turbo%20wizard.%0AThe%20original%20search%20was%3A%0A%E2%80%9Cleisure%3Dpitch%20and%20sport%3Dsoccer%20in%20Hungary%E2%80%9D%0A*%2F%0A%5Bout%3Ajson%5D%5Btimeout%3A25%5D%3B%0A%2F%2F%20fetch%20area%20%E2%80%9CBudapest%E2%80%9D%20to%20search%20in%0Aarea(id%3A3600037244)-%3E.searchArea%3B%0A%2F%2F%20gather%20results%0A(%0A%20%20%2F%2F%20query%20part%20for%3A%20%E2%80%9Cleisure%3Dpitch%20and%20sport%3Dsoccer%E2%80%9D%0A%20%20node%5B%22leisure%22%3D%22pitch%22%5D%5B%22sport%22%3D%22soccer%22%5D(area.searchArea)%3B%0A%20%20way%5B%22leisure%22%3D%22pitch%22%5D%5B%22sport%22%3D%22soccer%22%5D(area.searchArea)%3B%0A%20%20relation%5B%22leisure%22%3D%22pitch%22%5D%5B%22sport%22%3D%22soccer%22%5D(area.searchArea)%3B%0A)%3B%0A%2F%2F%20print%20results%0Aout%20body%3B%0A%3E%3B%0Aout%20skel%20qt%3B`
//   return toBlob(text).then(blob => sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob)))
// }

// Takes a timestamp (that counts time in seconds) and returns a pretty, formatted date-time string
export function timestampPrettyPrint(timestamp: string): string {
  return new Date(Number(timestamp) * 1000).toLocaleString("hu-HU")
}
