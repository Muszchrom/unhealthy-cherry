import { APIUser } from "@/interfaces/interfaces"
import NextAuth, { DefaultSession, NextAuthOptions, Session, User } from "next-auth"
import { JWT } from "next-auth/jwt"

import Credentials from "next-auth/providers/credentials"
import GithubProvider from "next-auth/providers/github"
import { signOut } from "next-auth/react"

/**
 * TODO
 * Edit backend so that it returns JWT and other, required info.
 * For this app JWT, userId, role is enough i think.
 * 
 * Once you've gathered all that info on next-server
 * Try to preload heder on server with navigation based on current session
 * Try to load an image on next-user/client/browser from java
 */
declare module "next-auth" {
  interface User extends APIUser {}

  interface Session {
    user: {
      id: User["user"]["id"],
      username: User["user"]["username"],
      isAdmin: User["user"]["isAdmin"],
      APIToken: User["token"]
    } & DefaultSession["user"]
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    id: User["user"]["id"]
    username: User["user"]["username"]
    isAdmin: User["user"]["isAdmin"]
    APIToken: User["token"]
  }
}

export const authOptions: NextAuthOptions = {
  session: {
    strategy: "jwt"
  },
  pages: {
    signIn: '/login',
  },
  providers: [
    GithubProvider({
      clientId: process.env.GITHUB_ID as string,
      clientSecret: process.env.GITHUB_SECRET as string,
    }),
    Credentials({
      name: "credentials",
      credentials: {
        username: { label: "username", type: "text" },
        password: { label: "password", type: "password" }
      },
      async authorize(credentials, req) {
        try {
          const res = await fetch("http://gateway:8081/auth/login", {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(credentials)
          });

          if (res.status === 403) {
            return null
          }

          const user: User = await res.json();
          if (res.ok && user) {
            return user;
          }
        } catch (e) {
          console.log(e);
        }
        return null;
      }
    })
  ],
  callbacks: {
    jwt({ token, user }: { token: JWT, user: User }) {
      if (user) {
        token.id = user.user.id;
        token.username = user.user.username;
        token.isAdmin = user.user.isAdmin;
        token.APIToken = user.token;
      }
      return token;
    },
    async session({ session, token }: { session: Session, token: JWT}) {
      const res = await fetch("http://gateway:8081/photos/categories", {
        headers: {Authorization: `Bearer ${token.APIToken}`}
      });
      if (res.status === 403) await signOut();
      if (!session.user) return session;
      session.user.id = token.id;
      session.user.username = token.username;
      session.user.isAdmin = token.isAdmin;
      session.user.APIToken = token.APIToken;
      return session;
    }
  }
}

const handler = NextAuth(authOptions);
export { handler as GET, handler as POST }