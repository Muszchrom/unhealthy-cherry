export interface Category {
  id: number,
  category: string,
  categoryAsPathVariable: string
}

export interface Place {
  id: number,
  place: string,
  placeAsPathVariable: string,
  category: Category
}

export interface Photo {
  id: number,
  fileName: string,
  fileExtension: string,
  place: Place,
  description: string,
  isBest: boolean,
  country: string,
  camera: string,
  datetime: number
}

export interface APIUser {
  token: string,
  user: {
    id: number,
    username: string,
    isAdmin: boolean
  }
}