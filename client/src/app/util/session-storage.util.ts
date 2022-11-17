import {fromBase64, toBase64} from "./encoding.util";

export function setItemInStorage(key: string, value: string): void {
  sessionStorage.setItem(toBase64(key), toBase64(value));
}

export function getItemFromStorage(key: string): string {
  const item = sessionStorage.getItem(toBase64(key));
  return item ? fromBase64(item) : null;
}

export function removeItemFromStorage(key: string): void {
  sessionStorage.removeItem(toBase64(key));
}
