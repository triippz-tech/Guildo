import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IWelcomeMessage, defaultValue } from 'app/shared/model/bot/welcome-message.model';

export const ACTION_TYPES = {
  FETCH_WELCOMEMESSAGE_LIST: 'welcomeMessage/FETCH_WELCOMEMESSAGE_LIST',
  FETCH_WELCOMEMESSAGE: 'welcomeMessage/FETCH_WELCOMEMESSAGE',
  CREATE_WELCOMEMESSAGE: 'welcomeMessage/CREATE_WELCOMEMESSAGE',
  UPDATE_WELCOMEMESSAGE: 'welcomeMessage/UPDATE_WELCOMEMESSAGE',
  DELETE_WELCOMEMESSAGE: 'welcomeMessage/DELETE_WELCOMEMESSAGE',
  RESET: 'welcomeMessage/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IWelcomeMessage>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type WelcomeMessageState = Readonly<typeof initialState>;

// Reducer

export default (state: WelcomeMessageState = initialState, action): WelcomeMessageState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_WELCOMEMESSAGE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_WELCOMEMESSAGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_WELCOMEMESSAGE):
    case REQUEST(ACTION_TYPES.UPDATE_WELCOMEMESSAGE):
    case REQUEST(ACTION_TYPES.DELETE_WELCOMEMESSAGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_WELCOMEMESSAGE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_WELCOMEMESSAGE):
    case FAILURE(ACTION_TYPES.CREATE_WELCOMEMESSAGE):
    case FAILURE(ACTION_TYPES.UPDATE_WELCOMEMESSAGE):
    case FAILURE(ACTION_TYPES.DELETE_WELCOMEMESSAGE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_WELCOMEMESSAGE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_WELCOMEMESSAGE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_WELCOMEMESSAGE):
    case SUCCESS(ACTION_TYPES.UPDATE_WELCOMEMESSAGE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_WELCOMEMESSAGE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/welcome-messages';

// Actions

export const getEntities: ICrudGetAllAction<IWelcomeMessage> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_WELCOMEMESSAGE_LIST,
  payload: axios.get<IWelcomeMessage>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IWelcomeMessage> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_WELCOMEMESSAGE,
    payload: axios.get<IWelcomeMessage>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IWelcomeMessage> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_WELCOMEMESSAGE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IWelcomeMessage> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_WELCOMEMESSAGE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IWelcomeMessage> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_WELCOMEMESSAGE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
