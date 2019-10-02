import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPunishment, defaultValue } from 'app/shared/model/bot/punishment.model';

export const ACTION_TYPES = {
  FETCH_PUNISHMENT_LIST: 'punishment/FETCH_PUNISHMENT_LIST',
  FETCH_PUNISHMENT: 'punishment/FETCH_PUNISHMENT',
  CREATE_PUNISHMENT: 'punishment/CREATE_PUNISHMENT',
  UPDATE_PUNISHMENT: 'punishment/UPDATE_PUNISHMENT',
  DELETE_PUNISHMENT: 'punishment/DELETE_PUNISHMENT',
  RESET: 'punishment/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPunishment>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PunishmentState = Readonly<typeof initialState>;

// Reducer

export default (state: PunishmentState = initialState, action): PunishmentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PUNISHMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PUNISHMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PUNISHMENT):
    case REQUEST(ACTION_TYPES.UPDATE_PUNISHMENT):
    case REQUEST(ACTION_TYPES.DELETE_PUNISHMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PUNISHMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PUNISHMENT):
    case FAILURE(ACTION_TYPES.CREATE_PUNISHMENT):
    case FAILURE(ACTION_TYPES.UPDATE_PUNISHMENT):
    case FAILURE(ACTION_TYPES.DELETE_PUNISHMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PUNISHMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PUNISHMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PUNISHMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_PUNISHMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PUNISHMENT):
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

const apiUrl = 'services/bot/api/punishments';

// Actions

export const getEntities: ICrudGetAllAction<IPunishment> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PUNISHMENT_LIST,
  payload: axios.get<IPunishment>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPunishment> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PUNISHMENT,
    payload: axios.get<IPunishment>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPunishment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PUNISHMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPunishment> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PUNISHMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPunishment> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PUNISHMENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
