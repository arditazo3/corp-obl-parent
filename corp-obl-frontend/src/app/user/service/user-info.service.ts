import {User} from '../model/user';
import {Injectable} from '@angular/core';
import {AuthorityEnum} from '../../shared/common/api/enum/authority.enum';

export interface UserTokenInStorage {
    user: User;
    token: string;
    langList: string[];
    langNotAvailableList: string[];
}

export interface LoginInfoInStorage {
    message: string;
    landingPage: string;
    userAndToken?: UserTokenInStorage;
}

@Injectable()
export class UserInfoService {

    public currentUserKey = 'currentUser';
    public storage: Storage = sessionStorage;

    constructor() {
    }

    // Store the user and token to the session storage like a string
    storeUserTokenInfo(userInfoString: string) {
        console.log('UserInfoService - storeUserTokenInfo');

        this.storage.setItem(this.currentUserKey, userInfoString);
    }

    // Remove the user and token from session storage
    removeUserTokenInfo() {
        this.storage.removeItem(this.currentUserKey);
    }

    // get the UserTokenInStorage from session storage
    getUserInfo(): UserTokenInStorage | null {
        //     console.log('UserInfoService - getUserInfo');

        try {
            const userInfoString: string = this.storage.getItem(this.currentUserKey);
            if (userInfoString) {
                return JSON.parse(this.storage.getItem(this.currentUserKey));
            } else {
                return null;
            }
        } catch (e) {
            return null;
        }
    }

    isLoggedIn(): boolean {
        console.log('UserInfoService - isLoggedIn');

        return !!this.storage.getItem(this.currentUserKey);
    }


    // get usernameString from session storage
    getUsername(): string {
        const userInStorage: UserTokenInStorage = this.getUserInfo();
        if (userInStorage !== null) {
            return userInStorage.user.fullName;
        }
        return 'no-user';
    }

    getUserLang(): string {
        const userInStorage: UserTokenInStorage = this.getUserInfo();
        if (userInStorage !== null) {
            return userInStorage.user.lang;
        }
        return 'EN';
    }

    getStoredToken(): string | null {
        const userInStorage: UserTokenInStorage = this.getUserInfo();
        if (userInStorage !== null) {
            return userInStorage.token;
        }
        return null;
    }

    isRoleAdmin(): boolean | null {
        const userInStorage: UserTokenInStorage = this.getUserInfo();
        if (userInStorage !== null) {
            return userInStorage.user.authorities.includes(AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN]);
        }
        return null;
    }

    isRoleForeign(): boolean | null {
        const userInStorage: UserTokenInStorage = this.getUserInfo();
        if (userInStorage !== null) {
            return userInStorage.user.authorities.includes(AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_FOREIGN]);
        }
        return null;
    }

    isRoleInland(): boolean | null {
        const userInStorage: UserTokenInStorage = this.getUserInfo();
        if (userInStorage !== null) {
            return userInStorage.user.authorities.includes(AuthorityEnum[AuthorityEnum.CORPOBLIG_BACKOFFICE_INLAND]);
        }
        return null;
    }

    getAuthorities(): AuthorityEnum[] | null {
        const userInStorage: UserTokenInStorage = this.getUserInfo();
        if (userInStorage !== null) {
            return userInStorage.user.authorities;
        }
        return null;
    }

    getLanguages(): string[] | null {
        const userInStorage: UserTokenInStorage = this.getUserInfo();
        if (userInStorage !== null) {
            return userInStorage.langList;
        }
        return null;
    }

    getLanguagesNotAvailable(): string[] | null {
        const userInStorage: UserTokenInStorage = this.getUserInfo();
        if (userInStorage !== null) {
            return userInStorage.langNotAvailableList;
        }
        return null;
    }
}
