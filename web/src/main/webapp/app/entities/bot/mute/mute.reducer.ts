import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IMute, defaultValue } from 'app/shared/model/bot/mute.model';

export const ACTION_TYPES = {
  FETCH_MUTE_LIST: 'mute/FETCH_MUTE_LIST',
  FETCH_MUTE: 'mute/FETCH_MUTE',
  CREATE_MUTE: 'mute/CREATE_MUTE',
  UPDATE_MUTE: 'mute/UPDATE_MUTE',
  DELETE_MUTE: 'mute/DELETE_MUTE',
  RESET: 'mute/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMute>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MuteState = Readonly<typeof initialState>;

// Reducer

export default (state: MuteState = initialState, action): MuteState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MUTE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MUTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MUTE):
    case REQUEST(ACTION_TYPES.UPDATE_MUTE):
    case REQUEST(ACTION_TYPES.DELETE_MUTE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_MUTE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MUTE):
    case FAILURE(ACTION_TYPES.CREATE_MUTE):
    case FAILURE(ACTION_TYPES.UPDATE_MUTE):
    case FAILURE(ACTION_TYPES.DELETE_MUTE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_MUTE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MUTE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MUTE):
    case SUCCESS(ACTION_TYPES.UPDATE_MUTE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MUTE):
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

const apiUrl = 'services/bot/api/mutes';

// Actions

export const getEntities: ICrudGetAllAction<IMute> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MUTE_LIST,
  payload: axios.get<IMute>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMute> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MUTE,
    payload: axios.get<IMute>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMute> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MUTE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMute> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MUTE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMute> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MUTE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
