import {DomSanitizer, SafeUrl} from "@angular/platform-browser";

export function toBase64(text: string): string {
  return Buffer.from(text).toString('base64');
}

export function fromBase64(text: string): string {
  return Buffer.from(text, 'base64').toString();
}

export function toBlob(text: string, type: string = "image/bmp"): Promise<Blob> {
  const url = `data:${type};base64,${text}`
  return fetch(url).then(res =>  res.blob());
}

export function toSafeUrl(text: string, sanitizer: DomSanitizer, type: string = "image/bmp"): Promise<SafeUrl> {
  return toBlob(text, type).then(blob => sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob)))
}
