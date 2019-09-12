package com.audiolaby.controller.enumeration;

public enum ResponseText {
    SUCCESS,
    MISSING_PARAMETER,	//Manque de données requises
    EMAIL_MUST_BE_UNIQUE, //	Email doit être unique
    PSEUDO_OR_EMAIL_MUST_BE_UNIQUE, //	Pseudo ou email doit être unique
    SHORT_PASSWORD, //	le mot de passe doit contenir au moins 6 caractères
    CONFIRM_PASSWORD, //	Le mot de passe ne correspond pas au mot de passe de confirmation
    INVALID_EMAIL, //	Le courrier électronique doit être un Adresse e-mail valide
    ACCOUNT_DISABLED, //	Compte désactivé
    INVALID_EMAIL_PASSWORD, //	Email ou mot de passe est incorrecte
    SOMETHING_WENT_WRONG, //	Quelque chose a mal tourné
    USER_NOT_FOUND, //	Aucun compte n'est associé à cette adresse e-mail
    INVALID_CODE_PASSWORD, //	Code ou email est incorrecte
    INVALID_IMAGE, //	L'entrée doit être un image
    INCORRECT_POST, //	L'identifiant de l'actualité est incorrecte
    DISABLED_DATA, //	Les données demandées sont desactivées
    UPDATE_REQUID,    //	Mise à jour requise
    INCORRECT_SECURITY_CODE,    //	Le code de sécurité est incorrecte
    INCORRECT_VERSION,    //	Une mise à jour obligatoire doit être faite
    INVALID_TOKEN,
    INTERNAL_ERROR,
    INCORRECT_PASSWORD
}
