import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IServerLogItem, defaultValue } from 'app/shared/model/bot/server-log-item.model';

export const ACTION_TYPES = {
  FETCH_SERVERLOGITEM_LIST: 'serverLogItem/FETCH_SERVERLOGITEM_LIST',
  FETCH_SERVERLOGITEM: 'serverLogItem/FETCH_SERVERLOGITEM',
  CREATE_SERVERLOGITEM: 'serverLogItem/CREATE_SERVERLOGITEM',
  UPDATE_SERVERLOGITEM: 'serverLogItem/UPDATE_SERVERLOGITEM',
  DELETE_SERVERLOGITEM: 'serverLogItem/DELETE_SERVERLOGITEM',
  RESET: 'serverLogItem/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IServerLogItem>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ServerLogItemState = Readonly<typeof initialState>;

// Reducer

export default (state: ServerLogItemState = initialState, action): ServerLogItemState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SERVERLOGITEM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SERVERLOGITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SERVERLOGITEM):
    case REQUEST(ACTION_TYPES.UPDATE_SERVERLOGITEM):
    case REQUEST(ACTION_TYPES.DELETE_SERVERLOGITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SERVERLOGITEM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SERVERLOGITEM):
    case FAILURE(ACTION_TYPES.CREATE_SERVERLOGITEM):
    case FAILURE(ACTION_TYPES.UPDATE_SERVERLOGITEM):
    case FAILURE(ACTION_TYPES.DELETE_SERVERLOGITEM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVERLOGITEM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVERLOGITEM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SERVERLOGITEM):
    case SUCCESS(ACTION_TYPES.UPDATE_SERVERLOGITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SERVERLOGITEM):
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

const apiUrl = 'services/bot/api/server-log-items';

// Actions

export const getEntities: ICrudGetAllAction<IServerLogItem> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SERVERLOGITEM_LIST,
  payload: axios.get<IServerLogItem>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IServerLogItem> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SERVERLOGITEM,
    payload: axios.get<IServerLogItem>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IServerLogItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SERVERLOGITEM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IServerLogItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SERVERLOGITEM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IServerLogItem> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SERVERLOGITEM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
