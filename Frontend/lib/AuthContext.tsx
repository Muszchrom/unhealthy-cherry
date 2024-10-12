"use client";

import React, { createContext, useContext, useEffect, useState } from "react";
import { checkJWT } from "./CheckJWT";

interface AuthContextProps {
  isAuthenticated: boolean;
  setIsAuthenticated: (value: boolean) => void;
}

export const AuthContext = createContext<AuthContextProps | undefined>(undefined);

export function AuthProvider({children}: {children: React.ReactNode}) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    checkJWT().then(val => setIsAuthenticated(val));
  }, [])

  return (
    <AuthContext.Provider value={{isAuthenticated, setIsAuthenticated}}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = (): AuthContextProps => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};