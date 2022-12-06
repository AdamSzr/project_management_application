import { ReactNode } from "react"
import AboutAppView from "../AboutComponent/AboutAppView"
import HomeMainView from "../HomeComponent/HomeMainView"
import SettingsComponent from "../SettingsComponent/SettigsComponent"
import ProjectsComponent from "../Projects/ProjectsComponent"
import UsersComponent from "../Users/UsersComponent"


export type MenuTab = {
    title: string,
    view: ReactNode,
    name: string
}

export const HomePage = { title: "Strona domowa", name: "home", view: <HomeMainView /> } as MenuTab

export class Pages {
    // static Home = 
    static About = { title: "O aplikacji", name: "about", view: <AboutAppView /> } as MenuTab
    static Projects = { title: "Projekty", name: "projects", view: <ProjectsComponent /> } as MenuTab
    static Users = { title: "Użytkownicy", name: "users", view: <UsersComponent /> } as MenuTab
    static Settings = { title: "Ustawienia", name: 'settigs', view: <SettingsComponent /> } as MenuTab
}